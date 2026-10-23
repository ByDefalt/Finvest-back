package defalt.account

import defalt.ApiIntegrationTest
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import kotlin.test.assertEquals

/** One account type per row: its table, the details sent at creation and at update, and a field that changes. */
data class AccountTypeCase(
    val accountTypeCode: String,
    val table: String,
    val createdDetails: String,
    val updatedDetails: String,
    val field: String,
    val createdValue: Any,
    val updatedValue: Any,
) {
    override fun toString() = accountTypeCode
}

fun accountTypeCases() =
    listOf(
        AccountTypeCase(
            "COMPTE_COURANT",
            "compte_courants",
            """{"accountId":0,"iban":"FR7630006000011234567890189","bic":"CMBRFR2B","accountNumber":"1","overdraftLimit":500,"holderName":"Romain"}""",
            """{"accountId":0,"iban":"FR7630006000011234567890189","bic":"CMBRFR2B","accountNumber":"1","overdraftLimit":500,"holderName":"Romain Rousval"}""",
            "holderName",
            "Romain",
            "Romain Rousval",
        ),
        AccountTypeCase(
            "LIVRET",
            "livrets",
            """{"accountId":0,"interestRate":1.7,"ceiling":22950}""",
            """{"accountId":0,"interestRate":1.7,"ceiling":10000}""",
            "ceiling",
            22950.0,
            10000.0,
        ),
        AccountTypeCase(
            "PEA",
            "peas",
            """{"accountId":0,"openingDate":"2022-03-15","depositLimit":150000}""",
            """{"accountId":0,"openingDate":"2023-03-15","depositLimit":150000}""",
            "openingDate",
            "2022-03-15",
            "2023-03-15",
        ),
        AccountTypeCase(
            "COMPTE_TITRES",
            "comptes_titres",
            """{"accountId":0,"accountNumber":"CT-1"}""",
            """{"accountId":0,"accountNumber":"CT-2"}""",
            "accountNumber",
            "CT-1",
            "CT-2",
        ),
        AccountTypeCase(
            "ASSURANCE_VIE",
            "assurances_vie",
            """{"accountId":0,"contractNumber":"AV-1","openingDate":"2021-05-10","managementTypeCode":"FREE_MANAGEMENT"}""",
            """{"accountId":0,"contractNumber":"AV-1","openingDate":"2021-05-10","managementTypeCode":"MANAGED"}""",
            "managementTypeCode",
            "FREE_MANAGEMENT",
            "MANAGED",
        ),
        AccountTypeCase(
            "PER",
            "pers",
            """{"accountId":0,"contractNumber":"PER-1","openingDate":"2025-09-01","managementTypeCode":"MANAGED"}""",
            """{"accountId":0,"contractNumber":"PER-2","openingDate":"2025-09-01","managementTypeCode":"MANAGED"}""",
            "contractNumber",
            "PER-1",
            "PER-2",
        ),
        AccountTypeCase(
            "PEE",
            "pees",
            """{"accountId":0,"openingDate":"2020-06-01","employer":"Crédit Mutuel Arkéa"}""",
            """{"accountId":0,"openingDate":"2020-06-01","employer":"Fortuneo"}""",
            "employer",
            "Crédit Mutuel Arkéa",
            "Fortuneo",
        ),
    )

/** Full lifecycle of every account type through the API, from the JSON body down to its details table. */
class AccountTypesApiIntegrationTest : ApiIntegrationTest() {
    @ParameterizedTest(name = "{0}")
    @MethodSource("defalt.account.AccountTypesApiIntegrationTestKt#accountTypeCases")
    fun `creates, reads, updates and deletes an account`(case: AccountTypeCase) {
        val token = token(ROMAIN)

        mockMvc
            .post("/accounts") {
                header(HttpHeaders.AUTHORIZATION, "Bearer $token")
                contentType = MediaType.APPLICATION_JSON
                content = body(case.accountTypeCode, case.createdDetails)
            }.andExpect { status { isCreated() } }
        val accountId = count("SELECT MAX(id) FROM accounts")
        assertEquals(1, count("SELECT COUNT(*) FROM ${case.table} WHERE account_id = $accountId"))

        mockMvc.get("/accounts") { header(HttpHeaders.AUTHORIZATION, "Bearer $token") }.andExpect {
            status { isOk() }
            jsonPath("$[0].account.accountTypeCode") { value(case.accountTypeCode) }
            jsonPath("$[0].details.accountId") { value(accountId) }
            jsonPath("$[0].details.${case.field}") { value(case.createdValue) }
        }

        mockMvc
            .put("/accounts/$accountId") {
                header(HttpHeaders.AUTHORIZATION, "Bearer $token")
                contentType = MediaType.APPLICATION_JSON
                content = body(case.accountTypeCode, case.updatedDetails)
            }.andExpect { status { isNoContent() } }

        mockMvc.get("/accounts") { header(HttpHeaders.AUTHORIZATION, "Bearer $token") }.andExpect {
            jsonPath("$[0].details.${case.field}") { value(case.updatedValue) }
        }

        mockMvc
            .delete("/accounts/$accountId") { header(HttpHeaders.AUTHORIZATION, "Bearer $token") }
            .andExpect { status { isNoContent() } }
        assertEquals(0, count("SELECT COUNT(*) FROM ${case.table} WHERE account_id = $accountId"))
        assertEquals(0, count("SELECT COUNT(*) FROM accounts"))
    }

    private fun body(
        accountTypeCode: String,
        details: String,
    ) = """
        {
          "accountTypeCode": "$accountTypeCode",
          "account": {
            "id": 0, "bankBic": "CMBRFR2B", "name": "Mon compte", "balance": 100, "currencyCode": "EUR",
            "createdAt": "2024-01-15T10:00:00", "closedAt": null, "accountStatusCode": "ACTIVE", "description": null
          },
          "details": $details,
          "owners": [{"ownershipPercentage":100,"email":"$ROMAIN"}]
        }
        """
}
