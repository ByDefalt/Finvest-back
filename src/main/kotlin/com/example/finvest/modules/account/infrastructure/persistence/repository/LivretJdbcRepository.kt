package com.example.finvest.modules.account.infrastructure.persistence.repository

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.domain.models.Livret
import com.example.finvest.modules.account.domain.repository.LivretRepository
import com.example.finvest.modules.account.infrastructure.persistence.mapper.toDomain
import com.example.finvest.modules.account.infrastructure.persistence.models.LivretEntity
import com.example.finvest.modules.account.infrastructure.persistence.queries.LivretQueries
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class LivretJdbcRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val logger: Logger
) : LivretRepository {

    override fun getLivretByUserId(userId: Long): List<Livret> {

        return jdbcTemplate.query(
            LivretQueries.GET_LIVRET_BY_USER_ID,
            MapSqlParameterSource()
                .addValue("userId", userId)
        ) { rs, _ ->
            LivretEntity(
                accountId = rs.getLong("accountId"),
                interestRate = rs.getBigDecimal("interestRate"),
                ceiling = rs.getBigDecimal("ceiling"),
            ).toDomain()
        }
    }
}
