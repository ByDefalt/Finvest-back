package com.example.finvest.modules.account.infrastructure.persistence.repository

import com.example.finvest.modules.account.domain.models.Account
import com.example.finvest.modules.account.domain.models.Money
import com.example.finvest.modules.account.domain.valueobject.AccountId
import com.example.finvest.modules.account.domain.valueobject.AccountStatusId
import com.example.finvest.modules.account.domain.valueobject.AccountTypeId
import com.example.finvest.modules.account.domain.valueobject.BankId
import com.example.finvest.modules.account.domain.valueobject.CurrencyId
import com.example.finvest.modules.shared.infrastructure.persistence.repository.JdbcRepositoryTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import java.math.BigDecimal
import java.time.LocalDateTime

class AccountJdbcRepositoryTest : JdbcRepositoryTest() {
    private lateinit var repository: AccountJdbcRepository

    @BeforeEach
    fun setUp() {
        repository = AccountJdbcRepository(jdbcTemplate, mock())
    }

    @Test
    fun `should create an account`() {
        val references = jdbcTemplate.insertReferenceData()
        val account =
            Account(
                id = AccountId(0),
                bankId = references.bankId,
                name = "Created account",
                balance = Money(BigDecimal("1234.56"), references.currencyId),
                createdAt = LocalDateTime.of(2024, 2, 3, 4, 5, 6),
                closedAt = null,
                accountStatusId = references.accountStatusId,
                description = "Created description",
                accountTypeId = references.accountTypeId,
            )

        val accountId = repository.createAccount(account)

        assertThat(accountId.value).isPositive()
        val row =
            jdbcTemplate.queryForMap(
                "SELECT * FROM accounts WHERE id = :id",
                MapSqlParameterSource().addValue("id", accountId.value),
            )
        assertThat(row["name"]).isEqualTo("Created account")
        assertThat(row["balance"].toString()).isEqualTo("1234.5600")
        assertThat(row["description"]).isEqualTo("Created description")
    }

    @Test
    fun `should update an account`() {
        val references = jdbcTemplate.insertReferenceData()
        val accountId = jdbcTemplate.insertAccount(references)
        val account =
            Account(
                id = accountId,
                bankId = BankId(references.bankId.value),
                name = "Updated account",
                balance = Money(BigDecimal("9876.54"), CurrencyId(references.currencyId.value)),
                createdAt = LocalDateTime.of(2025, 3, 4, 5, 6, 7),
                closedAt = LocalDateTime.of(2025, 4, 5, 6, 7, 8),
                accountStatusId = AccountStatusId(references.accountStatusId.value),
                description = "Updated description",
                accountTypeId = AccountTypeId(references.accountTypeId.value),
            )

        val result = repository.updateAccount(account)

        assertThat(result).isEqualTo(account)
        val row =
            jdbcTemplate.queryForMap(
                "SELECT * FROM accounts WHERE id = :id",
                MapSqlParameterSource().addValue("id", accountId.value),
            )
        assertThat(row["name"]).isEqualTo("Updated account")
        assertThat(row["balance"].toString()).isEqualTo("9876.5400")
        assertThat(row["description"]).isEqualTo("Updated description")
    }

    @Test
    fun `should delete an account`() {
        val references = jdbcTemplate.insertReferenceData()
        val accountId = jdbcTemplate.insertAccount(references)

        repository.deleteAccount(accountId)

        assertThat(
            jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM accounts WHERE id = :id",
                MapSqlParameterSource().addValue("id", accountId.value),
                Long::class.java,
            ),
        ).isZero()
    }
}
