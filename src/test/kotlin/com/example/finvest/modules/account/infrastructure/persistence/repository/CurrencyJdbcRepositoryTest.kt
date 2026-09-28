package com.example.finvest.modules.account.infrastructure.persistence.repository

import com.example.finvest.modules.account.domain.valueobject.CurrencyCode
import com.example.finvest.modules.shared.infrastructure.persistence.repository.JdbcRepositoryTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource

class CurrencyJdbcRepositoryTest : JdbcRepositoryTest() {
    private lateinit var repository: CurrencyJdbcRepository

    @BeforeEach
    fun setUp() {
        repository = CurrencyJdbcRepository(jdbcTemplate, mock())
    }

    @Test
    fun `should get currencies`() {
        val code = "TST"
        jdbcTemplate.update(
            "INSERT INTO currencies (code) VALUES (:code)",
            MapSqlParameterSource().addValue("code", code),
        )
        val currencyId =
            jdbcTemplate.queryForObject(
                "SELECT LAST_INSERT_ID()",
                MapSqlParameterSource(),
                Long::class.java,
            )!!

        val result = repository.getCurrencies()

        assertThat(result).containsEntry(currencyId, CurrencyCode(code))
    }
}
