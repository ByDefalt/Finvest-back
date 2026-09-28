package com.example.finvest.modules.account.infrastructure.persistence.repository

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.domain.models.Pea
import com.example.finvest.modules.account.domain.repository.PeaRepository
import com.example.finvest.modules.account.domain.valueobject.AccountId
import com.example.finvest.modules.account.infrastructure.persistence.mapper.toDomain
import com.example.finvest.modules.account.infrastructure.persistence.models.PeaEntity
import com.example.finvest.modules.account.infrastructure.persistence.queries.PeaQueries
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class PeaJdbcRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val logger: Logger,
) : PeaRepository {
    override fun getPeaByUserId(userId: Long): List<Pea> =
        jdbcTemplate.query(
            PeaQueries.GET_PEA_BY_USER_ID,
            MapSqlParameterSource()
                .addValue("userId", userId),
        ) { rs, _ ->
            PeaEntity(
                accountId = rs.getLong("accountId"),
                openingDate = rs.getDate("openingDate").toLocalDate(),
                depositLimit = rs.getBigDecimal("depositLimit"),
            ).toDomain()
        }

    override fun createPea(pea: Pea): AccountId {
        logger.info("Creating PEA for accountId: ${pea.accountId.value}")
        jdbcTemplate.update(
            PeaQueries.CREATE_PEA,
            MapSqlParameterSource()
                .addValue("accountId", pea.accountId.value)
                .addValue("openingDate", pea.openingDate)
                .addValue("depositLimit", pea.depositLimit),
        )
        logger.info("PEA created for accountId: ${pea.accountId.value}")
        return pea.accountId
    }

    override fun updatePea(pea: Pea): Pea {
        logger.info("Updating PEA for accountId: ${pea.accountId.value}")
        jdbcTemplate.update(
            PeaQueries.UPDATE_PEA_BY_ACCOUNT_ID,
            MapSqlParameterSource()
                .addValue("accountId", pea.accountId.value)
                .addValue("openingDate", pea.openingDate)
                .addValue("depositLimit", pea.depositLimit),
        )
        logger.info("PEA updated for accountId: ${pea.accountId.value}")
        return pea
    }

    override fun deletePea(accountId: AccountId) {
        logger.info("Deleting PEA for accountId: ${accountId.value}")
        jdbcTemplate.update(
            PeaQueries.DELETE_PEA_BY_ACCOUNT_ID,
            MapSqlParameterSource()
                .addValue("accountId", accountId.value),
        )
        logger.info("PEA deleted for accountId: ${accountId.value}")
    }
}
