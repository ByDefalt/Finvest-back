package com.example.finvest.modules.account.infrastructure.persistence.repository

import com.example.finvest.modules.account.domain.models.AssuranceVie
import com.example.finvest.modules.shared.infrastructure.persistence.repository.JdbcRepositoryTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import java.time.LocalDate

class AssuranceVieJdbcRepositoryTest : JdbcRepositoryTest() {
    private lateinit var repository: AssuranceVieJdbcRepository

    @BeforeEach
    fun setUp() {
        repository =
            AssuranceVieJdbcRepository(
                jdbcTemplate = jdbcTemplate,
                logger = mock(),
            )
    }

    @Test
    fun `should get assurance vie by user id`() {
        val references = jdbcTemplate.insertReferenceData()
        val userId = jdbcTemplate.insertUser()
        val accountId = jdbcTemplate.insertAccount(references)
        jdbcTemplate.insertAccountOwner(accountId, userId)
        val managementTypeId = jdbcTemplate.insertManagementType()
        jdbcTemplate.insertAssuranceVie(
            accountId,
            "AV-2021-000001",
            LocalDate.of(2021, 5, 10),
            managementTypeId,
        )

        val result = repository.getAssuranceVieByUserId(userId)

        assertThat(result).containsExactly(
            AssuranceVie(
                accountId,
                "AV-2021-000001",
                LocalDate.of(2021, 5, 10),
                managementTypeId,
            ),
        )
    }

    @Test
    fun `should create an assurance vie`() {
        val references = jdbcTemplate.insertReferenceData()
        val accountId = jdbcTemplate.insertAccount(references)
        val managementTypeId = jdbcTemplate.insertManagementType()
        val assuranceVie =
            AssuranceVie(
                accountId = accountId,
                contractNumber = "AV-CREATED",
                openingDate = LocalDate.of(2022, 1, 2),
                managementTypeId = managementTypeId,
            )

        val result = repository.createAssuranceVie(assuranceVie)

        assertThat(result).isEqualTo(accountId)
        val row =
            jdbcTemplate.queryForMap(
                "SELECT * FROM assurances_vie WHERE account_id = :accountId",
                MapSqlParameterSource().addValue("accountId", accountId.value),
            )
        assertThat(row["contract_number"]).isEqualTo("AV-CREATED")
    }

    @Test
    fun `should update an assurance vie`() {
        val references = jdbcTemplate.insertReferenceData()
        val accountId = jdbcTemplate.insertAccount(references)
        val managementTypeId = jdbcTemplate.insertManagementType()
        jdbcTemplate.update(
            """
            INSERT INTO assurances_vie (
                account_id, contract_number, opening_date, management_type_id
            )
            VALUES (:accountId, :contractNumber, :openingDate, :managementTypeId)
            """.trimIndent(),
            MapSqlParameterSource()
                .addValue("accountId", accountId.value)
                .addValue("contractNumber", "AV-OLD")
                .addValue("openingDate", LocalDate.of(2020, 1, 1))
                .addValue("managementTypeId", managementTypeId),
        )
        val assuranceVie =
            AssuranceVie(
                accountId = accountId,
                contractNumber = "AV-UPDATED",
                openingDate = LocalDate.of(2023, 3, 4),
                managementTypeId = managementTypeId,
            )

        val result = repository.updateAssuranceVie(assuranceVie)

        assertThat(result).isEqualTo(assuranceVie)
        assertThat(
            jdbcTemplate.queryForObject(
                "SELECT contract_number FROM assurances_vie WHERE account_id = :accountId",
                MapSqlParameterSource().addValue("accountId", accountId.value),
                String::class.java,
            ),
        ).isEqualTo("AV-UPDATED")
    }

    @Test
    fun `should delete an assurance vie`() {
        val references = jdbcTemplate.insertReferenceData()
        val accountId = jdbcTemplate.insertAccount(references)
        val managementTypeId = jdbcTemplate.insertManagementType()
        jdbcTemplate.update(
            """
            INSERT INTO assurances_vie (
                account_id, contract_number, opening_date, management_type_id
            )
            VALUES (:accountId, :contractNumber, :openingDate, :managementTypeId)
            """.trimIndent(),
            MapSqlParameterSource()
                .addValue("accountId", accountId.value)
                .addValue("contractNumber", "AV-DELETED")
                .addValue("openingDate", LocalDate.of(2020, 1, 1))
                .addValue("managementTypeId", managementTypeId),
        )

        repository.deleteAssuranceVie(accountId)

        assertThat(
            jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM assurances_vie WHERE account_id = :accountId",
                MapSqlParameterSource().addValue("accountId", accountId.value),
                Long::class.java,
            ),
        ).isZero()
    }
}
