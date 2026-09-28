package com.example.finvest.modules.account.infrastructure.persistence.repository

import com.example.finvest.modules.account.domain.models.Livret
import com.example.finvest.modules.account.domain.valueobject.AccountId
import com.example.finvest.modules.shared.infrastructure.persistence.repository.JdbcRepositoryTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import java.math.BigDecimal

class LivretJdbcRepositoryTest : JdbcRepositoryTest() {
    private lateinit var repository: LivretJdbcRepository

    @BeforeEach
    fun setUp() {
        repository = LivretJdbcRepository(jdbcTemplate, mock())
    }

    @Test
    fun `should get livrets by user id`() {
        val references = jdbcTemplate.insertReferenceData()
        val userId = jdbcTemplate.insertUser()
        val accountId = jdbcTemplate.insertAccount(references)
        jdbcTemplate.insertAccountOwner(accountId, userId)
        jdbcTemplate.insertLivret(accountId, BigDecimal("2.5000"), BigDecimal("22950.00"))

        val result = repository.getLivretByUserId(userId)

        assertThat(result).containsExactly(
            Livret(accountId, BigDecimal("2.5000"), BigDecimal("22950.0000")),
        )
    }

    @Test
    fun `should create a livret`() {
        val references = jdbcTemplate.insertReferenceData()
        val livret =
            Livret(
                jdbcTemplate.insertAccount(references),
                BigDecimal("1.2500"),
                BigDecimal("10000.00"),
            )

        val result = repository.createLivret(livret)

        assertThat(result).isEqualTo(livret.accountId)
        assertThat(jdbcTemplate.countLivret(livret.accountId)).isEqualTo(1)
    }

    @Test
    fun `should update a livret`() {
        val references = jdbcTemplate.insertReferenceData()
        val accountId = jdbcTemplate.insertAccount(references)
        jdbcTemplate.insertLivret(accountId, BigDecimal("1.0000"), BigDecimal("10000.00"))
        val livret = Livret(accountId, BigDecimal("2.0000"), BigDecimal("20000.00"))

        val result = repository.updateLivret(livret)

        assertThat(result).isEqualTo(livret)
        val row =
            jdbcTemplate.queryForMap(
                "SELECT * FROM livrets WHERE account_id = :accountId",
                MapSqlParameterSource().addValue("accountId", accountId.value),
            )
        assertThat(row["interest_rate"].toString()).isEqualTo("2.0000")
        assertThat(row["ceiling"].toString()).isEqualTo("20000.0000")
    }

    @Test
    fun `should delete a livret`() {
        val references = jdbcTemplate.insertReferenceData()
        val accountId = jdbcTemplate.insertAccount(references)
        jdbcTemplate.insertLivret(accountId, BigDecimal("1.0000"), BigDecimal("10000.00"))

        repository.deleteLivret(accountId)

        assertThat(jdbcTemplate.countLivret(accountId)).isZero()
    }
}

private fun org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate.insertLivret(
    accountId: AccountId,
    interestRate: BigDecimal,
    ceiling: BigDecimal,
) {
    update(
        "INSERT INTO livrets (account_id, interest_rate, ceiling) VALUES (:accountId, :interestRate, :ceiling)",
        MapSqlParameterSource()
            .addValue("accountId", accountId.value)
            .addValue("interestRate", interestRate)
            .addValue("ceiling", ceiling),
    )
}

private fun org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate.countLivret(accountId: AccountId): Long =
    queryForObject(
        "SELECT COUNT(*) FROM livrets WHERE account_id = :accountId",
        MapSqlParameterSource().addValue("accountId", accountId.value),
        Long::class.java,
    )!!
