package com.example.finvest.modules.account.infrastructure.persistence.repository

import com.example.finvest.modules.shared.infrastructure.persistence.repository.JdbcRepositoryTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource

class BankJdbcRepositoryTest : JdbcRepositoryTest() {
    private lateinit var repository: BankJdbcRepository

    @BeforeEach
    fun setUp() {
        repository = BankJdbcRepository(jdbcTemplate, mock())
    }

    @Test
    fun `should get banks`() {
        val name = "Test bank"
        val bic = "TESTBIC"
        jdbcTemplate.update(
            """
            INSERT INTO banks (name, bic, logo)
            VALUES (:name, :bic, :logo)
            """.trimIndent(),
            MapSqlParameterSource()
                .addValue("name", name)
                .addValue("bic", bic)
                .addValue("logo", "https://example.com/logo.svg"),
        )
        val bankId =
            jdbcTemplate.queryForObject(
                "SELECT LAST_INSERT_ID()",
                MapSqlParameterSource(),
                Long::class.java,
            )!!

        val result = repository.getBanks()

        assertThat(result[bankId]?.name).isEqualTo(name)
        assertThat(result[bankId]?.bic?.value).isEqualTo(bic)
        assertThat(result[bankId]?.logo).isEqualTo("https://example.com/logo.svg")
    }
}
