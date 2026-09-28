package com.example.finvest.modules.account.infrastructure.persistence.repository

import com.example.finvest.modules.account.domain.models.CompteTitre
import com.example.finvest.modules.account.domain.valueobject.AccountId
import com.example.finvest.modules.shared.infrastructure.persistence.repository.JdbcRepositoryTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource

class CompteTitreJdbcRepositoryTest : JdbcRepositoryTest() {
    private lateinit var repository: CompteTitreJdbcRepository

    @BeforeEach
    fun setUp() {
        repository = CompteTitreJdbcRepository(jdbcTemplate, mock())
    }

    @Test
    fun `should get compte titres by user id`() {
        val references = jdbcTemplate.insertReferenceData()
        val userId = jdbcTemplate.insertUser()
        val accountId = jdbcTemplate.insertAccount(references)
        jdbcTemplate.insertAccountOwner(accountId, userId)
        jdbcTemplate.insertCompteTitre(accountId, "CT-GET")

        val result = repository.getCompteTitreByUserId(userId)

        assertThat(result).containsExactly(CompteTitre(accountId, "CT-GET"))
    }

    @Test
    fun `should create a compte titre`() {
        val references = jdbcTemplate.insertReferenceData()
        val compteTitre = CompteTitre(jdbcTemplate.insertAccount(references), "CT-CREATED")

        val result = repository.createCompteTitre(compteTitre)

        assertThat(result).isEqualTo(compteTitre.accountId)
        assertThat(jdbcTemplate.count("comptes_titres", compteTitre.accountId)).isEqualTo(1)
    }

    @Test
    fun `should update a compte titre`() {
        val references = jdbcTemplate.insertReferenceData()
        val accountId = jdbcTemplate.insertAccount(references)
        jdbcTemplate.insertCompteTitre(accountId, "CT-OLD")
        val compteTitre = CompteTitre(accountId, "CT-UPDATED")

        val result = repository.updateCompteTitre(compteTitre)

        assertThat(result).isEqualTo(compteTitre)
        assertThat(
            jdbcTemplate.queryForObject(
                "SELECT account_number FROM comptes_titres WHERE account_id = :accountId",
                MapSqlParameterSource().addValue("accountId", accountId.value),
                String::class.java,
            ),
        ).isEqualTo("CT-UPDATED")
    }

    @Test
    fun `should delete a compte titre`() {
        val references = jdbcTemplate.insertReferenceData()
        val accountId = jdbcTemplate.insertAccount(references)
        jdbcTemplate.insertCompteTitre(accountId, "CT-DELETED")

        repository.deleteCompteTitre(accountId)

        assertThat(jdbcTemplate.count("comptes_titres", accountId)).isZero()
    }
}

private fun org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate.insertCompteTitre(
    accountId: AccountId,
    accountNumber: String,
) {
    update(
        "INSERT INTO comptes_titres (account_id, account_number) VALUES (:accountId, :accountNumber)",
        MapSqlParameterSource()
            .addValue("accountId", accountId.value)
            .addValue("accountNumber", accountNumber),
    )
}

private fun org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate.count(
    table: String,
    accountId: AccountId,
): Long =
    queryForObject(
        "SELECT COUNT(*) FROM $table WHERE account_id = :accountId",
        MapSqlParameterSource().addValue("accountId", accountId.value),
        Long::class.java,
    )!!
