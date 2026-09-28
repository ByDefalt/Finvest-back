package com.example.finvest.modules.account.infrastructure.persistence.repository

import com.example.finvest.modules.account.domain.models.AccountStatus
import com.example.finvest.modules.shared.infrastructure.persistence.repository.JdbcRepositoryTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource

class AccountStatusJdbcRepositoryTest : JdbcRepositoryTest() {
    private lateinit var repository: AccountStatusJdbcRepository

    @BeforeEach
    fun setUp() {
        repository = AccountStatusJdbcRepository(jdbcTemplate, mock())
    }

    @Test
    fun `should get account statuses`() {
        val statusId =
            jdbcTemplate.run {
                update(
                    "INSERT INTO account_statuses (code) VALUES (:code)",
                    MapSqlParameterSource().addValue("code", AccountStatus.ACTIVE.name),
                )
                queryForObject(
                    "SELECT LAST_INSERT_ID()",
                    MapSqlParameterSource(),
                    Long::class.java,
                )!!
            }

        val result = repository.getAccountStatuses()

        assertThat(result).containsEntry(statusId, AccountStatus.ACTIVE)
    }
}
