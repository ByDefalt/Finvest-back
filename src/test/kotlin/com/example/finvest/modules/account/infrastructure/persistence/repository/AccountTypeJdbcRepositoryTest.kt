package com.example.finvest.modules.account.infrastructure.persistence.repository

import com.example.finvest.modules.account.domain.valueobject.AccountType
import com.example.finvest.modules.shared.infrastructure.persistence.repository.JdbcRepositoryTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource

class AccountTypeJdbcRepositoryTest : JdbcRepositoryTest() {
    private lateinit var repository: AccountTypeJdbcRepository

    @BeforeEach
    fun setUp() {
        repository = AccountTypeJdbcRepository(jdbcTemplate, mock())
    }

    @Test
    fun `should get account types`() {
        val code = "TEST_ACCOUNT_TYPE"
        jdbcTemplate.update(
            "INSERT INTO account_types (code) VALUES (:code)",
            MapSqlParameterSource().addValue("code", code),
        )
        val typeId =
            jdbcTemplate.queryForObject(
                "SELECT LAST_INSERT_ID()",
                MapSqlParameterSource(),
                Long::class.java,
            )!!

        val result = repository.getAccountTypes()

        assertThat(result).containsEntry(typeId, AccountType(code))
    }
}
