package com.example.finvest.modules.account.infrastructure.persistence.repository

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.domain.models.AssuranceVie
import com.example.finvest.modules.account.domain.repository.AssuranceVieRepository
import com.example.finvest.modules.account.domain.valueobject.AccountId
import com.example.finvest.modules.account.infrastructure.persistence.mapper.toDomain
import com.example.finvest.modules.account.infrastructure.persistence.models.AssuranceVieEntity
import com.example.finvest.modules.account.infrastructure.persistence.queries.AssuranceVieQueries
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class AssuranceVieJdbcRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val logger: Logger,
) : AssuranceVieRepository {
    override fun getAssuranceVieByUserId(userId: Long): List<AssuranceVie> {
        logger.info("Fetching assurance vie for userId: $userId")
        return jdbcTemplate.query(
            AssuranceVieQueries.GET_ASSURANCE_VIE_BY_USER_ID,
            MapSqlParameterSource()
                .addValue("userId", userId),
        ) { rs, _ ->
            AssuranceVieEntity(
                accountId = rs.getLong("accountId"),
                contractNumber = rs.getString("contractNumber"),
                openingDate = rs.getDate("openingDate").toLocalDate(),
                managementTypeId = rs.getLong("managementTypeId"),
            ).toDomain()
        }
    }

    override fun createAssuranceVie(assuranceVie: AssuranceVie): AccountId {
        logger.info("Creating assurance vie for accountId: ${assuranceVie.accountId}")
        jdbcTemplate.update(
            AssuranceVieQueries.CREATE_ASSURANCE_VIE,
            MapSqlParameterSource()
                .addValue("accountId", assuranceVie.accountId.value)
                .addValue("contractNumber", assuranceVie.contractNumber)
                .addValue("openingDate", assuranceVie.openingDate)
                .addValue("managementTypeId", assuranceVie.managementTypeId),
        )
        logger.info("assuranceVie created: $assuranceVie")
        return assuranceVie.accountId
    }

    override fun updateAssuranceVie(assuranceVie: AssuranceVie): AssuranceVie {
        logger.info("Updating assurance vie for accountId: ${assuranceVie.accountId}")
        jdbcTemplate.update(
            AssuranceVieQueries.UPDATE_ASSURANCE_VIE_BY_ACCOUNT_ID,
            MapSqlParameterSource()
                .addValue("accountId", assuranceVie.accountId.value)
                .addValue("contractNumber", assuranceVie.contractNumber)
                .addValue("openingDate", assuranceVie.openingDate)
                .addValue("managementTypeId", assuranceVie.managementTypeId),
        )
        logger.info("assuranceVie updated: $assuranceVie")
        return assuranceVie
    }

    override fun deleteAssuranceVie(accountId: AccountId) {
        logger.info("Deleting assurance vie for accountId: ${accountId.value}")
        jdbcTemplate.update(
            AssuranceVieQueries.DELETE_ASSURANCE_VIE_BY_ACCOUNT_ID,
            MapSqlParameterSource()
                .addValue("accountId", accountId.value),
        )
        logger.info("assuranceVie deleted for accountId: ${accountId.value}")
    }
}
