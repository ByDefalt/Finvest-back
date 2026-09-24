package com.example.finvest.modules.account.infrastructure.persistence.repository

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.domain.models.Pee
import com.example.finvest.modules.account.domain.repository.PeeRepository
import com.example.finvest.modules.account.infrastructure.persistence.mapper.toDomain
import com.example.finvest.modules.account.infrastructure.persistence.models.PeeEntity
import com.example.finvest.modules.account.infrastructure.persistence.queries.PeeQueries
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class PeeJdbcRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val logger: Logger
) : PeeRepository {

    override fun getPeeByUserId(userId: Long): List<Pee> {
        return jdbcTemplate.query(
            PeeQueries.GET_PEE_BY_USER_ID,
            MapSqlParameterSource()
                .addValue("userId", userId)
        ) { rs, _ ->
            PeeEntity(
                accountId = rs.getLong("accountId"),
                openingDate = rs.getDate("openingDate").toLocalDate(),
                employer = rs.getString("employer"),
            ).toDomain()
        }
    }
}