package com.example.finvest.modules.account.infrastructure.persistence.repository

import com.example.finvest.modules.shared.infrastructure.persistence.repository.JdbcRepositoryTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import java.time.LocalDate


class AssuranceVieJdbcRepositoryTest : JdbcRepositoryTest() {
    private lateinit var repository: AssuranceVieJdbcRepository

    @BeforeEach
    fun setUp() {
        repository = AssuranceVieJdbcRepository(
            jdbcTemplate = jdbcTemplate,
            logger = mock()
        )
    }

    @Test
    fun `should get assurance vie by user id`() {
        // Given

        // User
        jdbcTemplate.update(
            """
            INSERT INTO users (email, password)
            VALUES (:email, :password)
            """.trimIndent(),
            MapSqlParameterSource()
                .addValue("email", "assurance-vie-test@example.com")
                .addValue("password", "password")
        )

        val userId = jdbcTemplate.queryForObject(
            """
            SELECT id
            FROM users
            WHERE email = :email
            """.trimIndent(),
            MapSqlParameterSource()
                .addValue("email", "assurance-vie-test@example.com"),
            Long::class.java
        )!!

        // Bank
        jdbcTemplate.update(
            """
            INSERT INTO banks (name)
            VALUES (:name)
            """.trimIndent(),
            MapSqlParameterSource()
                .addValue("name", "Test Bank")
        )

        val bankId = jdbcTemplate.queryForObject(
            """
            SELECT id
            FROM banks
            WHERE name = :name
            """.trimIndent(),
            MapSqlParameterSource()
                .addValue("name", "Test Bank"),
            Long::class.java
        )!!

        // Currency
        jdbcTemplate.update(
            """
            INSERT INTO currencies (code)
            VALUES (:code)
            """.trimIndent(),
            MapSqlParameterSource()
                .addValue("code", "EUR")
        )

        val currencyId = jdbcTemplate.queryForObject(
            """
            SELECT id
            FROM currencies
            WHERE code = :code
            """.trimIndent(),
            MapSqlParameterSource()
                .addValue("code", "EUR"),
            Long::class.java
        )!!

        // Account status
        jdbcTemplate.update(
            """
            INSERT INTO account_statuses (code)
            VALUES (:code)
            """.trimIndent(),
            MapSqlParameterSource()
                .addValue("code", "OPEN")
        )

        val accountStatusId = jdbcTemplate.queryForObject(
            """
            SELECT id
            FROM account_statuses
            WHERE code = :code
            """.trimIndent(),
            MapSqlParameterSource()
                .addValue("code", "OPEN"),
            Long::class.java
        )!!

        // Account type
        jdbcTemplate.update(
            """
            INSERT INTO account_types (code)
            VALUES (:code)
            """.trimIndent(),
            MapSqlParameterSource()
                .addValue("code", "ASSURANCE_VIE")
        )

        val accountTypeId = jdbcTemplate.queryForObject(
            """
            SELECT id
            FROM account_types
            WHERE code = :code
            """.trimIndent(),
            MapSqlParameterSource()
                .addValue("code", "ASSURANCE_VIE"),
            Long::class.java
        )!!

        // Account
        jdbcTemplate.update(
            """
            INSERT INTO accounts (
                bank_id,
                name,
                balance,
                currency_id,
                account_status_id,
                account_type_id,
                description
            )
            VALUES (
                :bankId,
                :name,
                :balance,
                :currencyId,
                :accountStatusId,
                :accountTypeId,
                :description
            )
            """.trimIndent(),
            MapSqlParameterSource()
                .addValue("bankId", bankId)
                .addValue("name", "Assurance Vie Test")
                .addValue("balance", 10_000.00)
                .addValue("currencyId", currencyId)
                .addValue("accountStatusId", accountStatusId)
                .addValue("accountTypeId", accountTypeId)
                .addValue("description", "Test assurance vie")
        )

        val accountId = jdbcTemplate.queryForObject(
            """
            SELECT id
            FROM accounts
            WHERE name = :name
            """.trimIndent(),
            MapSqlParameterSource()
                .addValue("name", "Assurance Vie Test"),
            Long::class.java
        )!!

        // Account owner
        jdbcTemplate.update(
            """
            INSERT INTO account_owners (
                account_id,
                user_id,
                name,
                ownership_percentage
            )
            VALUES (
                :accountId,
                :userId,
                :name,
                :ownershipPercentage
            )
            """.trimIndent(),
            MapSqlParameterSource()
                .addValue("accountId", accountId)
                .addValue("userId", userId)
                .addValue("name", "Romain")
                .addValue("ownershipPercentage", 100.00)
        )

        // Management type
        jdbcTemplate.update(
            """
            INSERT INTO management_types (code)
            VALUES (:code)
            """.trimIndent(),
            MapSqlParameterSource()
                .addValue("code", "FREE_MANAGEMENT")
        )

        val managementTypeId = jdbcTemplate.queryForObject(
            """
            SELECT id
            FROM management_types
            WHERE code = :code
            """.trimIndent(),
            MapSqlParameterSource()
                .addValue("code", "FREE_MANAGEMENT"),
            Long::class.java
        )!!

        // Assurance vie
        jdbcTemplate.update(
            """
            INSERT INTO assurances_vie (
                account_id,
                contract_number,
                opening_date,
                management_type_id
            )
            VALUES (
                :accountId,
                :contractNumber,
                :openingDate,
                :managementTypeId
            )
            """.trimIndent(),
            MapSqlParameterSource()
                .addValue("accountId", accountId)
                .addValue("contractNumber", "AV-2021-000001")
                .addValue("openingDate", LocalDate.of(2021, 5, 10))
                .addValue("managementTypeId", managementTypeId)
        )

        // When
        val result = repository.getAssuranceVieByUserId(userId)

        // Then
        assertThat(result).hasSize(1)

        val assuranceVie = result.first()

        assertThat(assuranceVie.contractNumber)
            .isEqualTo("AV-2021-000001")

        assertThat(assuranceVie.openingDate)
            .isEqualTo(LocalDate.of(2021, 5, 10))

        assertThat(assuranceVie.managementTypeId)
            .isEqualTo(managementTypeId)
    }
}