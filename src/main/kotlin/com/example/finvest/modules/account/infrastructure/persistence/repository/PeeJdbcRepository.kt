package com.example.finvest.modules.account.infrastructure.persistence.repository

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.domain.models.Pee
import com.example.finvest.modules.account.domain.repository.PeeRepository
import com.example.finvest.modules.account.domain.valueobject.AccountId
import com.example.finvest.modules.account.infrastructure.persistence.mapper.toDomain
import com.example.finvest.modules.account.infrastructure.persistence.models.PeeEntity
import com.example.finvest.modules.account.infrastructure.persistence.queries.PeeQueries
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class PeeJdbcRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val logger: Logger,
) : PeeRepository {
    override fun getPeeByUserId(userId: Long): List<Pee> =
        jdbcTemplate.query(
            PeeQueries.GET_PEE_BY_USER_ID,
            MapSqlParameterSource()
                .addValue("userId", userId),
        ) { rs, _ ->
            PeeEntity(
                accountId = rs.getLong("accountId"),
                openingDate = rs.getDate("openingDate").toLocalDate(),
                employer = rs.getString("employer"),
            ).toDomain()
        }

    override fun createPee(pee: Pee): AccountId {
        logger.info("Creating PEE for accountId: ${pee.accountId.value}")
        jdbcTemplate.update(
            PeeQueries.CREATE_PEE,
            MapSqlParameterSource()
                .addValue("accountId", pee.accountId.value)
                .addValue("openingDate", pee.openingDate)
                .addValue("employer", pee.employer),
        )
        logger.info("PEE created for accountId: ${pee.accountId.value}")
        return pee.accountId
    }

    override fun updatePee(pee: Pee): Pee {
        logger.info("Updating PEE for accountId: ${pee.accountId.value}")
        jdbcTemplate.update(
            PeeQueries.UPDATE_PEE_BY_ACCOUNT_ID,
            MapSqlParameterSource()
                .addValue("accountId", pee.accountId.value)
                .addValue("openingDate", pee.openingDate)
                .addValue("employer", pee.employer),
        )
        logger.info("PEE updated for accountId: ${pee.accountId.value}")
        return pee
    }

    override fun deletePee(accountId: AccountId) {
        logger.info("Deleting PEE for accountId: ${accountId.value}")
        jdbcTemplate.update(
            PeeQueries.DELETE_PEE_BY_ACCOUNT_ID,
            MapSqlParameterSource()
                .addValue("accountId", accountId.value),
        )
        logger.info("PEE deleted for accountId: ${accountId.value}")
    }
}
