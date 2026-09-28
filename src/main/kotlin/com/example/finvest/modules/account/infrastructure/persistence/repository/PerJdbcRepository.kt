package com.example.finvest.modules.account.infrastructure.persistence.repository

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.domain.models.Per
import com.example.finvest.modules.account.domain.repository.PerRepository
import com.example.finvest.modules.account.domain.valueobject.AccountId
import com.example.finvest.modules.account.infrastructure.persistence.mapper.toDomain
import com.example.finvest.modules.account.infrastructure.persistence.models.PerEntity
import com.example.finvest.modules.account.infrastructure.persistence.queries.PerQueries
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class PerJdbcRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val logger: Logger,
) : PerRepository {
    override fun getPerByUserId(userId: Long): List<Per> =
        jdbcTemplate.query(
            PerQueries.GET_PER_BY_USER_ID,
            MapSqlParameterSource()
                .addValue("userId", userId),
        ) { rs, _ ->
            PerEntity(
                accountId = rs.getLong("accountId"),
                contractNumber = rs.getString("contractNumber"),
                openingDate = rs.getDate("openingDate").toLocalDate(),
                managementTypeId = rs.getLong("managementTypeId"),
            ).toDomain()
        }

    override fun createPer(per: Per): AccountId {
        logger.info("Creating PER for accountId: ${per.accountId.value}")
        jdbcTemplate.update(
            PerQueries.CREATE_PER,
            MapSqlParameterSource()
                .addValue("accountId", per.accountId.value)
                .addValue("contractNumber", per.contractNumber)
                .addValue("openingDate", per.openingDate)
                .addValue("managementTypeId", per.managementTypeId),
        )
        logger.info("PER created for accountId: ${per.accountId.value}")
        return per.accountId
    }

    override fun updatePer(per: Per): Per {
        logger.info("Updating PER for accountId: ${per.accountId.value}")
        jdbcTemplate.update(
            PerQueries.UPDATE_PER_BY_ACCOUNT_ID,
            MapSqlParameterSource()
                .addValue("accountId", per.accountId.value)
                .addValue("contractNumber", per.contractNumber)
                .addValue("openingDate", per.openingDate)
                .addValue("managementTypeId", per.managementTypeId),
        )
        logger.info("PER updated for accountId: ${per.accountId.value}")
        return per
    }

    override fun deletePer(accountId: AccountId) {
        logger.info("Deleting PER for accountId: ${accountId.value}")
        jdbcTemplate.update(
            PerQueries.DELETE_PER_BY_ACCOUNT_ID,
            MapSqlParameterSource()
                .addValue("accountId", accountId.value),
        )
        logger.info("PER deleted for accountId: ${accountId.value}")
    }
}
