package com.example.finvest.modules.account.infrastructure.persistence.repository

import com.example.finvest.modules.account.domain.valueobject.AccountId
import com.example.finvest.modules.account.domain.valueobject.AccountStatusId
import com.example.finvest.modules.account.domain.valueobject.AccountTypeId
import com.example.finvest.modules.account.domain.valueobject.BankId
import com.example.finvest.modules.account.domain.valueobject.CurrencyId
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

internal data class AccountRepositoryReferenceData(
    val bankId: BankId,
    val currencyId: CurrencyId,
    val accountStatusId: AccountStatusId,
    val accountTypeId: AccountTypeId,
)

internal fun NamedParameterJdbcTemplate.insertReferenceData(
    suffix: String = UUID.randomUUID().toString().replace("-", ""),
): AccountRepositoryReferenceData {
    val bankId =
        insertAndGetId(
            """
            INSERT INTO banks (name, bic)
            VALUES (:name, :bic)
            """.trimIndent(),
            MapSqlParameterSource()
                .addValue("name", "Test Bank $suffix")
                .addValue("bic", "TSTB${suffix.take(8).uppercase()}"),
        )
    val currencyId =
        insertAndGetId(
            """
            INSERT INTO currencies (code)
            VALUES (:code)
            """.trimIndent(),
            MapSqlParameterSource().addValue("code", "T${suffix.take(9).uppercase()}"),
        )
    val accountStatusId =
        insertAndGetId(
            """
            INSERT INTO account_statuses (code)
            VALUES (:code)
            """.trimIndent(),
            MapSqlParameterSource().addValue("code", "ACTIVE"),
        )
    val accountTypeId =
        insertAndGetId(
            """
            INSERT INTO account_types (code)
            VALUES (:code)
            """.trimIndent(),
            MapSqlParameterSource().addValue("code", "TEST_$suffix"),
        )
    return AccountRepositoryReferenceData(
        bankId = BankId(bankId),
        currencyId = CurrencyId(currencyId),
        accountStatusId = AccountStatusId(accountStatusId),
        accountTypeId = AccountTypeId(accountTypeId),
    )
}

internal fun NamedParameterJdbcTemplate.insertUser(email: String = "repository-test-${UUID.randomUUID()}@example.com"): Long =
    insertAndGetId(
        """
        INSERT INTO users (email, password)
        VALUES (:email, :password)
        """.trimIndent(),
        MapSqlParameterSource()
            .addValue("email", email)
            .addValue("password", "password"),
    )

internal fun NamedParameterJdbcTemplate.insertAccount(
    references: AccountRepositoryReferenceData,
    name: String = "Test account",
): AccountId =
    AccountId(
        insertAndGetId(
            """
            INSERT INTO accounts (
                bank_id, name, balance, currency_id, created_at,
                account_status_id, description, account_type_id
            )
            VALUES (
                :bankId, :name, :balance, :currencyId, :createdAt,
                :accountStatusId, :description, :accountTypeId
            )
            """.trimIndent(),
            MapSqlParameterSource()
                .addValue("bankId", references.bankId.value)
                .addValue("name", name)
                .addValue("balance", BigDecimal("1000.00"))
                .addValue("currencyId", references.currencyId.value)
                .addValue("createdAt", LocalDateTime.of(2024, 1, 2, 3, 4, 5))
                .addValue("accountStatusId", references.accountStatusId.value)
                .addValue("description", "Test description")
                .addValue("accountTypeId", references.accountTypeId.value),
        ),
    )

internal fun NamedParameterJdbcTemplate.insertAccountOwner(
    accountId: AccountId,
    userId: Long,
) {
    update(
        """
        INSERT INTO account_owners (
            account_id, user_id, name, ownership_percentage
        )
        VALUES (:accountId, :userId, :name, :ownershipPercentage)
        """.trimIndent(),
        MapSqlParameterSource()
            .addValue("accountId", accountId.value)
            .addValue("userId", userId)
            .addValue("name", "Test owner")
            .addValue("ownershipPercentage", BigDecimal("100.00")),
    )
}

internal fun NamedParameterJdbcTemplate.insertManagementType(code: String = "FREE_MANAGEMENT"): Long =
    insertAndGetId(
        """
        INSERT INTO management_types (code)
        VALUES (:code)
        """.trimIndent(),
        MapSqlParameterSource().addValue("code", code),
    )

internal fun NamedParameterJdbcTemplate.insertAssuranceVie(
    accountId: AccountId,
    contractNumber: String,
    openingDate: LocalDate,
    managementTypeId: Long,
) {
    update(
        """
        INSERT INTO assurances_vie (
            account_id, contract_number, opening_date, management_type_id
        )
        VALUES (:accountId, :contractNumber, :openingDate, :managementTypeId)
        """.trimIndent(),
        MapSqlParameterSource()
            .addValue("accountId", accountId.value)
            .addValue("contractNumber", contractNumber)
            .addValue("openingDate", openingDate)
            .addValue("managementTypeId", managementTypeId),
    )
}

private fun NamedParameterJdbcTemplate.insertAndGetId(
    sql: String,
    parameters: MapSqlParameterSource,
): Long {
    update(sql, parameters)
    return queryForObject(
        "SELECT LAST_INSERT_ID()",
        MapSqlParameterSource(),
        Long::class.java,
    )!!
}
