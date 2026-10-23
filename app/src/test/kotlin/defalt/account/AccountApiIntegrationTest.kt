package defalt.account

import defalt.ApiIntegrationTest
import defalt.valueobject.Email
import org.hamcrest.Matchers.containsInAnyOrder
import org.hamcrest.Matchers.hasItem
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.test.web.servlet.ResultActionsDsl
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import kotlin.test.assertEquals

/**
 * End-to-end tests of the /accounts endpoints: authentication + HTTP + JSON + validation + use cases + MariaDB.
 * The database is reset before each test; requests are authenticated with real access tokens.
 */
class AccountApiIntegrationTest : ApiIntegrationTest() {
    // ------------------------------------------------------------------
    // Authentication
    // ------------------------------------------------------------------

    @Test
    fun `rejects requests without access token`() {
        mockMvc.get("/accounts").andExpect { status { isUnauthorized() } }
        mockMvc
            .post("/accounts") {
                contentType = MediaType.APPLICATION_JSON
                content = peaBody()
            }.andExpect { status { isUnauthorized() } }
        mockMvc.delete("/accounts/1").andExpect { status { isUnauthorized() } }

        assertEquals(0, countAccounts())
    }

    @Test
    fun `rejects requests without access token with a translatable code`() {
        mockMvc.get("/accounts").andExpect {
            status { isUnauthorized() }
            jsonPath("$.code") { value("AUTHENTICATION_REQUIRED") }
        }
    }

    @Test
    fun `rejects an invalid access token`() {
        mockMvc.get("/accounts") { header(HttpHeaders.AUTHORIZATION, "Bearer not-a-jwt") }.andExpect {
            status { isUnauthorized() }
            jsonPath("$.code") { value("INVALID_ACCESS_TOKEN") }
        }
    }

    // ------------------------------------------------------------------
    // HTTP errors
    // ------------------------------------------------------------------

    @Test
    fun `an unknown route answers 404 not found`() {
        mockMvc.get("/unknown") { header(HttpHeaders.AUTHORIZATION, "Bearer ${token(ROMAIN)}") }.andExpect {
            status { isNotFound() }
            jsonPath("$.code") { value("NOT_FOUND") }
        }
    }

    @Test
    fun `an unsupported method answers 405`() {
        mockMvc.patch("/accounts/1") { header(HttpHeaders.AUTHORIZATION, "Bearer ${token(ROMAIN)}") }.andExpect {
            status { isMethodNotAllowed() }
            jsonPath("$.code") { value("METHOD_NOT_ALLOWED") }
        }
    }

    @Test
    fun `a body that is not json answers 415`() {
        mockMvc
            .post("/accounts") {
                header(HttpHeaders.AUTHORIZATION, "Bearer ${token(ROMAIN)}")
                contentType = MediaType.TEXT_PLAIN
                content = "hello"
            }.andExpect {
                status { isUnsupportedMediaType() }
                jsonPath("$.code") { value("UNSUPPORTED_MEDIA_TYPE") }
            }
    }

    @Test
    fun `an account id that is not a number has an invalid format`() {
        deleteAccount("abc").andExpect {
            status { isBadRequest() }
            jsonPath("$.code") { value("INVALID_FORMAT") }
            jsonPath("$.field") { value("accountId") }
        }
    }

    @Test
    fun `an account still referenced by other data cannot be deleted`() {
        createAccount(peaBody()).andExpect { status { isCreated() } }
        val accountId = accountIdByName("PEA Romain")
        jdbcTemplate.update(
            "INSERT INTO account_balance_snapshots (account_id, balance, currency_code, snapshot_date) VALUES (:id, 100, 'EUR', CURRENT_DATE)",
            MapSqlParameterSource("id", accountId),
        )

        deleteAccount(accountId).andExpect {
            status { isConflict() }
            jsonPath("$.code") { value("INTEGRITY_VIOLATION") }
        }

        assertEquals(1, countAccounts())
    }

    @Test
    fun `rejects a refresh token used as access token`() {
        val refreshToken = tokenGenerator.generateRefreshToken(userId(ROMAIN), Email(ROMAIN))

        mockMvc
            .get("/accounts") { header(HttpHeaders.AUTHORIZATION, "Bearer $refreshToken") }
            .andExpect { status { isUnauthorized() } }
    }

    @Test
    fun `each user only sees their own accounts`() {
        createAccount(peaBody()).andExpect { status { isCreated() } }
        insertPeaOwnedByTestUser()

        getAccounts(token(TEST)).andExpect {
            status { isOk() }
            jsonPath("$", hasSize<Any>(1))
            jsonPath("$[0].account.name") { value("PEA Test") }
        }
    }

    // ------------------------------------------------------------------
    // Create
    // ------------------------------------------------------------------

    @Test
    fun `creates an account shared with an external user and returns it in the list`() {
        createAccount(peaBody(owners = OWNERS_SHARED_WITH_JOHN)).andExpect { status { isCreated() } }

        getAccounts().andExpect {
            status { isOk() }
            jsonPath("$", hasSize<Any>(1))
            jsonPath("$[0].account.name") { value("PEA Romain") }
            jsonPath("$[0].account.accountTypeCode") { value("PEA") }
            jsonPath("$[0].account.bank.name") { value("Fortuneo") }
            jsonPath("$[0].details.openingDate") { value("2022-03-15") }
            jsonPath("$[0].details.depositLimit") { value(150000.0) }
            jsonPath("$[0].owners[*].email", containsInAnyOrder("romain@example.com", "john.doe@example.com"))
        }
    }

    @Test
    fun `rejects invalid details with the failing fields`() {
        createAccount(
            body(
                "COMPTE_COURANT",
                """{"accountId":0,"iban":"","bic":"CMBRFR2B","accountNumber":"1","overdraftLimit":-1,"holderName":"Romain"}""",
            ),
        ).andExpect {
            status { isBadRequest() }
            jsonPath("$.code") { value("VALIDATION_FAILED") }
            jsonPath("$.errors[*].field", containsInAnyOrder("details.iban", "details.overdraftLimit"))
        }

        assertEquals(0, countAccounts())
    }

    @Test
    fun `rejects an unknown account type`() {
        createAccount(body("UNKNOWN", "{}")).andExpect {
            status { isBadRequest() }
            jsonPath("$.code") { value("INVALID_FORMAT") }
        }
    }

    @Test
    fun `rejects ownership percentages that do not add up to 100`() {
        createAccount(peaBody(owners = """[{"ownershipPercentage":90,"email":"romain@example.com"}]""")).andExpect {
            status { isBadRequest() }
            jsonPath("$.code") { value("OWNERSHIP_TOTAL_MISMATCH") }
        }

        assertEquals(0, countAccounts())
    }

    @Test
    fun `rejects an owner email that matches no user`() {
        createAccount(
            peaBody(
                owners = """[{"ownershipPercentage":50,"email":"romain@example.com"},
                             {"ownershipPercentage":50,"email":"ghost@example.com"}]""",
            ),
        ).andExpect {
            status { isNotFound() }
            jsonPath("$.code") { value("OWNER_NOT_FOUND") }
            jsonPath("$.emails", hasItem("ghost@example.com"))
        }

        assertEquals(0, countAccounts())
    }

    @Test
    fun `rolls back the account when its details cannot be saved`() {
        val compteCourant =
            """{"accountId":0,"iban":"FR7630006000011234567890189","bic":"CMBRFR2B","accountNumber":"1","overdraftLimit":0,"holderName":"Romain"}"""
        createAccount(body("COMPTE_COURANT", compteCourant)).andExpect { status { isCreated() } }

        // Same IBAN: the account row is inserted, then the details insert violates the unique constraint
        createAccount(body("COMPTE_COURANT", compteCourant)).andExpect {
            status { isConflict() }
            jsonPath("$.code") { value("DUPLICATE") }
        }

        assertEquals(1, countAccounts())
    }

    // ------------------------------------------------------------------
    // Update
    // ------------------------------------------------------------------

    @Test
    fun `updates the account, its details and its owners`() {
        createAccount(peaBody()).andExpect { status { isCreated() } }
        val accountId = accountIdByName("PEA Romain")

        updateAccount(
            accountId,
            peaBody(
                name = "PEA renamed",
                details = """{"accountId":0,"openingDate":"2023-01-01","depositLimit":20000}""",
                owners = OWNERS_SHARED_WITH_JOHN,
            ),
        ).andExpect { status { isNoContent() } }

        getAccounts().andExpect {
            jsonPath("$[0].account.id") { value(accountId) }
            jsonPath("$[0].account.name") { value("PEA renamed") }
            jsonPath("$[0].details.openingDate") { value("2023-01-01") }
            jsonPath("$[0].details.depositLimit") { value(20000.0) }
            jsonPath("$[0].owners[*].email", containsInAnyOrder("romain@example.com", "john.doe@example.com"))
        }
    }

    @Test
    fun `refuses to change the account type`() {
        createAccount(peaBody()).andExpect { status { isCreated() } }

        updateAccount(accountIdByName("PEA Romain"), body("COMPTE_TITRES", """{"accountId":0,"accountNumber":"CT-1"}"""))
            .andExpect {
                status { isBadRequest() }
                jsonPath("$.code") { value("ACCOUNT_TYPE_IMMUTABLE") }
            }
    }

    @Test
    fun `denies the update of an account owned by another user`() {
        val accountId = insertPeaOwnedByTestUser()

        updateAccount(accountId, peaBody(name = "Hacked")).andExpect {
            status { isForbidden() }
            jsonPath("$.code") { value("ACCOUNT_ACCESS_DENIED") }
        }

        assertEquals(accountId, accountIdByName("PEA Test"))
    }

    @Test
    fun `fails to update an account that does not exist`() {
        updateAccount(999L, peaBody()).andExpect {
            status { isNotFound() }
            jsonPath("$.code") { value("ACCOUNT_NOT_FOUND") }
        }
    }

    // ------------------------------------------------------------------
    // Delete
    // ------------------------------------------------------------------

    @Test
    fun `deletes the account with its details and owners`() {
        createAccount(peaBody()).andExpect { status { isCreated() } }
        val accountId = accountIdByName("PEA Romain")

        deleteAccount(accountId).andExpect { status { isNoContent() } }

        assertEquals(0, countAccounts())
        assertEquals(0, countRows("peas", accountId))
        assertEquals(0, countRows("account_owners", accountId))
        deleteAccount(accountId).andExpect {
            status { isNotFound() }
            jsonPath("$.code") { value("ACCOUNT_NOT_FOUND") }
        }
    }

    @Test
    fun `denies the deletion of an account owned by another user`() {
        val accountId = insertPeaOwnedByTestUser()

        deleteAccount(accountId).andExpect {
            status { isForbidden() }
            jsonPath("$.code") { value("ACCOUNT_ACCESS_DENIED") }
        }

        assertEquals(1, countAccounts())
    }

    // ------------------------------------------------------------------
    // Helpers
    // ------------------------------------------------------------------

    private fun createAccount(
        json: String,
        token: String = token(ROMAIN),
    ): ResultActionsDsl =
        mockMvc.post("/accounts") {
            header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            contentType = MediaType.APPLICATION_JSON
            content = json
        }

    private fun updateAccount(
        accountId: Long,
        json: String,
        token: String = token(ROMAIN),
    ): ResultActionsDsl =
        mockMvc.put("/accounts/$accountId") {
            header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            contentType = MediaType.APPLICATION_JSON
            content = json
        }

    private fun deleteAccount(
        accountId: Any,
        token: String = token(ROMAIN),
    ): ResultActionsDsl = mockMvc.delete("/accounts/$accountId") { header(HttpHeaders.AUTHORIZATION, "Bearer $token") }

    private fun getAccounts(token: String = token(ROMAIN)): ResultActionsDsl =
        mockMvc.get("/accounts") { header(HttpHeaders.AUTHORIZATION, "Bearer $token") }

    private fun peaBody(
        name: String = "PEA Romain",
        details: String = """{"accountId":0,"openingDate":"2022-03-15","depositLimit":150000}""",
        owners: String = OWNER_ROMAIN,
    ) = body("PEA", details, name, owners)

    private fun body(
        accountTypeCode: String,
        details: String,
        name: String = "PEA Romain",
        owners: String = OWNER_ROMAIN,
    ) = """
        {
          "accountTypeCode": "$accountTypeCode",
          "account": {
            "id": 0, "bankBic": "FTNOFRP1", "name": "$name", "balance": 25000.00, "currencyCode": "EUR",
            "createdAt": "2024-01-15T10:00:00", "closedAt": null, "accountStatusCode": "ACTIVE", "description": null
          },
          "details": $details,
          "owners": $owners
        }
    """

    private fun insertPeaOwnedByTestUser(): Long {
        jdbcTemplate.update(
            """
            INSERT INTO accounts (bank_bic, name, balance, currency_code, account_status_code, account_type_code)
            VALUES ('FTNOFRP1', 'PEA Test', 100, 'EUR', 'ACTIVE', 'PEA')
            """,
            MapSqlParameterSource(),
        )
        val accountId = accountIdByName("PEA Test")
        val params = MapSqlParameterSource("accountId", accountId)
        jdbcTemplate.update(
            "INSERT INTO peas (account_id, opening_date, deposit_limit) VALUES (:accountId, '2024-06-10', 150000)",
            params,
        )
        jdbcTemplate.update(
            """
            INSERT INTO account_owners (account_id, user_id, ownership_percentage)
            SELECT :accountId, u.id, 100 FROM users u
            JOIN personal_informations pi ON pi.id = u.personal_information_id
            WHERE pi.email = 'test@example.com'
            """,
            params,
        )
        return accountId
    }

    private fun accountIdByName(name: String): Long =
        jdbcTemplate.queryForObject("SELECT id FROM accounts WHERE name = :name", MapSqlParameterSource("name", name), Long::class.java)!!

    private fun countAccounts(): Int =
        jdbcTemplate.queryForObject("SELECT COUNT(*) FROM accounts", MapSqlParameterSource(), Int::class.java)!!

    private fun countRows(
        table: String,
        accountId: Long,
    ): Int =
        jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM $table WHERE account_id = :accountId",
            MapSqlParameterSource("accountId", accountId),
            Int::class.java,
        )!!

    private companion object {
        const val OWNER_ROMAIN = """[{"ownershipPercentage":100,"email":"romain@example.com"}]"""
        const val OWNERS_SHARED_WITH_JOHN =
            """[{"ownershipPercentage":50,"email":"romain@example.com"},{"ownershipPercentage":50,"email":"john.doe@example.com"}]"""
    }
}
