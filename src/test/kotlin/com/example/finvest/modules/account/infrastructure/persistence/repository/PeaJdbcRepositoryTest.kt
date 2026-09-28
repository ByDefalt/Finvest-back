package com.example.finvest.modules.account.infrastructure.persistence.repository

import com.example.finvest.modules.account.domain.models.Pea
import com.example.finvest.modules.account.domain.valueobject.AccountId
import com.example.finvest.modules.shared.infrastructure.persistence.repository.JdbcRepositoryTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import java.math.BigDecimal
import java.time.LocalDate

class PeaJdbcRepositoryTest : JdbcRepositoryTest() {
    private lateinit var repository: PeaJdbcRepository

    @BeforeEach
    fun setUp() {
        repository = PeaJdbcRepository(jdbcTemplate, mock())
    }

    @Test
    fun `should get peas by user id`() {
        val references = jdbcTemplate.insertReferenceData()
        val userId = jdbcTemplate.insertUser()
        val accountId = jdbcTemplate.insertAccount(references)
        jdbcTemplate.insertAccountOwner(accountId, userId)
        jdbcTemplate.insertPea(accountId, LocalDate.of(2020, 1, 2), BigDecimal("150000.00"))

        val result = repository.getPeaByUserId(userId)

        assertThat(result).containsExactly(
            Pea(accountId, LocalDate.of(2020, 1, 2), BigDecimal("150000.0000")),
        )
    }

    @Test
    fun `should create a pea`() {
        val references = jdbcTemplate.insertReferenceData()
        val pea =
            Pea(
                jdbcTemplate.insertAccount(references),
                LocalDate.of(2021, 2, 3),
                BigDecimal("100000.00"),
            )

        val result = repository.createPea(pea)

        assertThat(result).isEqualTo(pea.accountId)
        assertThat(jdbcTemplate.countPea(pea.accountId)).isEqualTo(1)
    }

    @Test
    fun `should update a pea`() {
        val references = jdbcTemplate.insertReferenceData()
        val accountId = jdbcTemplate.insertAccount(references)
        jdbcTemplate.insertPea(accountId, LocalDate.of(2020, 1, 1), BigDecimal("100000.00"))
        val pea = Pea(accountId, LocalDate.of(2022, 3, 4), BigDecimal("200000.00"))

        val result = repository.updatePea(pea)

        assertThat(result).isEqualTo(pea)
        val row =
            jdbcTemplate.queryForMap(
                "SELECT * FROM peas WHERE account_id = :accountId",
                MapSqlParameterSource().addValue("accountId", accountId.value),
            )
        assertThat(row["deposit_limit"].toString()).isEqualTo("200000.0000")
    }

    @Test
    fun `should delete a pea`() {
        val references = jdbcTemplate.insertReferenceData()
        val accountId = jdbcTemplate.insertAccount(references)
        jdbcTemplate.insertPea(accountId, LocalDate.of(2020, 1, 1), BigDecimal("100000.00"))

        repository.deletePea(accountId)

        assertThat(jdbcTemplate.countPea(accountId)).isZero()
    }
}

private fun org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate.insertPea(
    accountId: AccountId,
    openingDate: LocalDate,
    depositLimit: BigDecimal,
) {
    update(
        "INSERT INTO peas (account_id, opening_date, deposit_limit) VALUES (:accountId, :openingDate, :depositLimit)",
        MapSqlParameterSource()
            .addValue("accountId", accountId.value)
            .addValue("openingDate", openingDate)
            .addValue("depositLimit", depositLimit),
    )
}

private fun org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate.countPea(accountId: AccountId): Long =
    queryForObject(
        "SELECT COUNT(*) FROM peas WHERE account_id = :accountId",
        MapSqlParameterSource().addValue("accountId", accountId.value),
        Long::class.java,
    )!!
