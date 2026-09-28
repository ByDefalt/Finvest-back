package com.example.finvest.modules.account.infrastructure.persistence.repository

import com.example.finvest.modules.account.domain.models.CompteCourant
import com.example.finvest.modules.account.domain.valueobject.AccountId
import com.example.finvest.modules.shared.infrastructure.persistence.repository.JdbcRepositoryTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import java.math.BigDecimal

class CompteCourantJdbcRepositoryTest : JdbcRepositoryTest() {
    private lateinit var repository: CompteCourantJdbcRepository

    @BeforeEach
    fun setUp() {
        repository = CompteCourantJdbcRepository(jdbcTemplate, mock())
    }

    @Test
    fun `should get compte courants by user id`() {
        val references = jdbcTemplate.insertReferenceData()
        val userId = jdbcTemplate.insertUser()
        val accountId = jdbcTemplate.insertAccount(references)
        jdbcTemplate.insertAccountOwner(accountId, userId)
        jdbcTemplate.insertCompteCourant(accountId, "FR761234", "TESTBIC", "1234")

        val result = repository.getCompteCourantByUserId(userId)

        assertThat(result).containsExactly(
            CompteCourant(
                accountId = accountId,
                iban = "FR761234",
                bic = "TESTBIC",
                accountNumber = "1234",
                overdraftLimit = BigDecimal("500.0000"),
                holderName = "Test holder",
            ),
        )
    }

    @Test
    fun `should create a compte courant`() {
        val references = jdbcTemplate.insertReferenceData()
        val compteCourant =
            CompteCourant(
                accountId = jdbcTemplate.insertAccount(references),
                iban = "FR766543",
                bic = "CREATEBIC",
                accountNumber = "5678",
                overdraftLimit = BigDecimal("250.00"),
                holderName = "Created holder",
            )

        val result = repository.createCompteCourant(compteCourant)

        assertThat(result).isEqualTo(compteCourant.accountId)
        assertThat(jdbcTemplate.countCompteCourant(compteCourant.accountId)).isEqualTo(1)
    }

    @Test
    fun `should update a compte courant`() {
        val references = jdbcTemplate.insertReferenceData()
        val accountId = jdbcTemplate.insertAccount(references)
        jdbcTemplate.insertCompteCourant(accountId, "FR761111", "OLDBIC", "1111")
        val compteCourant =
            CompteCourant(
                accountId = accountId,
                iban = "FR772222",
                bic = "UPDATEDBIC",
                accountNumber = "2222",
                overdraftLimit = BigDecimal("900.00"),
                holderName = "Updated holder",
            )

        val result = repository.updateCompteCourant(compteCourant)

        assertThat(result).isEqualTo(compteCourant)
        val row =
            jdbcTemplate.queryForMap(
                "SELECT * FROM compte_courants WHERE account_id = :accountId",
                MapSqlParameterSource().addValue("accountId", accountId.value),
            )
        assertThat(row["iban"]).isEqualTo("FR772222")
        assertThat(row["holder_name"]).isEqualTo("Updated holder")
    }

    @Test
    fun `should delete a compte courant`() {
        val references = jdbcTemplate.insertReferenceData()
        val accountId = jdbcTemplate.insertAccount(references)
        jdbcTemplate.insertCompteCourant(accountId, "FR763333", "DELBIC", "3333")

        repository.deleteCompteCourant(accountId)

        assertThat(jdbcTemplate.countCompteCourant(accountId)).isZero()
    }
}

private fun org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate.insertCompteCourant(
    accountId: AccountId,
    iban: String,
    bic: String,
    accountNumber: String,
) {
    update(
        """
        INSERT INTO compte_courants (
            account_id, iban, bic, account_number, overdraft_limit, holder_name
        )
        VALUES (:accountId, :iban, :bic, :accountNumber, :overdraftLimit, :holderName)
        """.trimIndent(),
        MapSqlParameterSource()
            .addValue("accountId", accountId.value)
            .addValue("iban", iban)
            .addValue("bic", bic)
            .addValue("accountNumber", accountNumber)
            .addValue("overdraftLimit", BigDecimal("500.00"))
            .addValue("holderName", "Test holder"),
    )
}

private fun org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate.countCompteCourant(accountId: AccountId): Long =
    queryForObject(
        "SELECT COUNT(*) FROM compte_courants WHERE account_id = :accountId",
        MapSqlParameterSource().addValue("accountId", accountId.value),
        Long::class.java,
    )!!
