package com.example.finvest.modules.account.infrastructure.persistence.repository

import com.example.finvest.modules.account.domain.models.Per
import com.example.finvest.modules.account.domain.valueobject.AccountId
import com.example.finvest.modules.shared.infrastructure.persistence.repository.JdbcRepositoryTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import java.time.LocalDate

class PerJdbcRepositoryTest : JdbcRepositoryTest() {
    private lateinit var repository: PerJdbcRepository

    @BeforeEach
    fun setUp() {
        repository = PerJdbcRepository(jdbcTemplate, mock())
    }

    @Test
    fun `should get pers by user id`() {
        val references = jdbcTemplate.insertReferenceData()
        val userId = jdbcTemplate.insertUser()
        val accountId = jdbcTemplate.insertAccount(references)
        jdbcTemplate.insertAccountOwner(accountId, userId)
        val managementTypeId = jdbcTemplate.insertManagementType()
        jdbcTemplate.insertPer(accountId, "PER-GET", LocalDate.of(2020, 1, 2), managementTypeId)

        val result = repository.getPerByUserId(userId)

        assertThat(result).containsExactly(
            Per(accountId, "PER-GET", LocalDate.of(2020, 1, 2), managementTypeId),
        )
    }

    @Test
    fun `should create a per`() {
        val references = jdbcTemplate.insertReferenceData()
        val managementTypeId = jdbcTemplate.insertManagementType()
        val per =
            Per(
                jdbcTemplate.insertAccount(references),
                "PER-CREATED",
                LocalDate.of(2021, 2, 3),
                managementTypeId,
            )

        val result = repository.createPer(per)

        assertThat(result).isEqualTo(per.accountId)
        assertThat(jdbcTemplate.countPer(per.accountId)).isEqualTo(1)
    }

    @Test
    fun `should update a per`() {
        val references = jdbcTemplate.insertReferenceData()
        val accountId = jdbcTemplate.insertAccount(references)
        val managementTypeId = jdbcTemplate.insertManagementType()
        jdbcTemplate.insertPer(accountId, "PER-OLD", LocalDate.of(2020, 1, 1), managementTypeId)
        val per = Per(accountId, "PER-UPDATED", LocalDate.of(2022, 3, 4), managementTypeId)

        val result = repository.updatePer(per)

        assertThat(result).isEqualTo(per)
        assertThat(
            jdbcTemplate.queryForObject(
                "SELECT contract_number FROM pers WHERE account_id = :accountId",
                MapSqlParameterSource().addValue("accountId", accountId.value),
                String::class.java,
            ),
        ).isEqualTo("PER-UPDATED")
    }

    @Test
    fun `should delete a per`() {
        val references = jdbcTemplate.insertReferenceData()
        val accountId = jdbcTemplate.insertAccount(references)
        val managementTypeId = jdbcTemplate.insertManagementType()
        jdbcTemplate.insertPer(accountId, "PER-DELETED", LocalDate.of(2020, 1, 1), managementTypeId)

        repository.deletePer(accountId)

        assertThat(jdbcTemplate.countPer(accountId)).isZero()
    }
}

private fun org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate.insertPer(
    accountId: AccountId,
    contractNumber: String,
    openingDate: LocalDate,
    managementTypeId: Long,
) {
    update(
        """
        INSERT INTO pers (account_id, contract_number, opening_date, management_type_id)
        VALUES (:accountId, :contractNumber, :openingDate, :managementTypeId)
        """.trimIndent(),
        MapSqlParameterSource()
            .addValue("accountId", accountId.value)
            .addValue("contractNumber", contractNumber)
            .addValue("openingDate", openingDate)
            .addValue("managementTypeId", managementTypeId),
    )
}

private fun org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate.countPer(accountId: AccountId): Long =
    queryForObject(
        "SELECT COUNT(*) FROM pers WHERE account_id = :accountId",
        MapSqlParameterSource().addValue("accountId", accountId.value),
        Long::class.java,
    )!!
