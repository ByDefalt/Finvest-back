package com.example.finvest.modules.account.infrastructure.persistence.repository

import com.example.finvest.modules.account.domain.models.Pee
import com.example.finvest.modules.account.domain.valueobject.AccountId
import com.example.finvest.modules.shared.infrastructure.persistence.repository.JdbcRepositoryTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import java.time.LocalDate

class PeeJdbcRepositoryTest : JdbcRepositoryTest() {
    private lateinit var repository: PeeJdbcRepository

    @BeforeEach
    fun setUp() {
        repository = PeeJdbcRepository(jdbcTemplate, mock())
    }

    @Test
    fun `should get pees by user id`() {
        val references = jdbcTemplate.insertReferenceData()
        val userId = jdbcTemplate.insertUser()
        val accountId = jdbcTemplate.insertAccount(references)
        jdbcTemplate.insertAccountOwner(accountId, userId)
        jdbcTemplate.insertPee(accountId, LocalDate.of(2020, 1, 2), "Old employer")

        val result = repository.getPeeByUserId(userId)

        assertThat(result).containsExactly(
            Pee(accountId, LocalDate.of(2020, 1, 2), "Old employer"),
        )
    }

    @Test
    fun `should create a pee`() {
        val references = jdbcTemplate.insertReferenceData()
        val pee =
            Pee(
                jdbcTemplate.insertAccount(references),
                LocalDate.of(2021, 2, 3),
                "Created employer",
            )

        val result = repository.createPee(pee)

        assertThat(result).isEqualTo(pee.accountId)
        assertThat(jdbcTemplate.countPee(pee.accountId)).isEqualTo(1)
    }

    @Test
    fun `should update a pee`() {
        val references = jdbcTemplate.insertReferenceData()
        val accountId = jdbcTemplate.insertAccount(references)
        jdbcTemplate.insertPee(accountId, LocalDate.of(2020, 1, 1), "Old employer")
        val pee = Pee(accountId, LocalDate.of(2022, 3, 4), "Updated employer")

        val result = repository.updatePee(pee)

        assertThat(result).isEqualTo(pee)
        val row =
            jdbcTemplate.queryForMap(
                "SELECT * FROM pees WHERE account_id = :accountId",
                MapSqlParameterSource().addValue("accountId", accountId.value),
            )
        assertThat(row["employer"]).isEqualTo("Updated employer")
    }

    @Test
    fun `should delete a pee`() {
        val references = jdbcTemplate.insertReferenceData()
        val accountId = jdbcTemplate.insertAccount(references)
        jdbcTemplate.insertPee(accountId, LocalDate.of(2020, 1, 1), "Employer")

        repository.deletePee(accountId)

        assertThat(jdbcTemplate.countPee(accountId)).isZero()
    }
}

private fun org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate.insertPee(
    accountId: AccountId,
    openingDate: LocalDate,
    employer: String,
) {
    update(
        "INSERT INTO pees (account_id, opening_date, employer) VALUES (:accountId, :openingDate, :employer)",
        MapSqlParameterSource()
            .addValue("accountId", accountId.value)
            .addValue("openingDate", openingDate)
            .addValue("employer", employer),
    )
}

private fun org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate.countPee(accountId: AccountId): Long =
    queryForObject(
        "SELECT COUNT(*) FROM pees WHERE account_id = :accountId",
        MapSqlParameterSource().addValue("accountId", accountId.value),
        Long::class.java,
    )!!
