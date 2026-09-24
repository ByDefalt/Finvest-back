package com.example.finvest.modules.account.infrastructure.persistence.repository

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.domain.models.Pea
import com.example.finvest.modules.account.domain.repository.PeaRepository
import com.example.finvest.modules.account.infrastructure.persistence.mapper.toDomain
import com.example.finvest.modules.account.infrastructure.persistence.models.PeaEntity
import com.example.finvest.modules.account.infrastructure.persistence.queries.PeaQueries
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class PeaJdbcRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val logger: Logger
) : PeaRepository {

    override fun getPeaByUserId(userId: Long): List<Pea> {
        return jdbcTemplate.query(
            PeaQueries.GET_PEA_BY_USER_ID,
            MapSqlParameterSource()
                .addValue("userId", userId)
        ) { rs, _ ->
            PeaEntity(
                accountId = rs.getLong("accountId"),
                openingDate = rs.getDate("openingDate").toLocalDate(),
                depositLimit = rs.getBigDecimal("depositLimit"),
            ).toDomain()
        }
    }
}
