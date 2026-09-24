package com.example.finvest.modules.account.infrastructure.persistence.repository

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.domain.models.Per
import com.example.finvest.modules.account.domain.repository.PerRepository
import com.example.finvest.modules.account.infrastructure.persistence.mapper.toDomain
import com.example.finvest.modules.account.infrastructure.persistence.models.PerEntity
import com.example.finvest.modules.account.infrastructure.persistence.queries.PerQueries
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class PerJdbcRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val logger: Logger
) : PerRepository {

    override fun getPerByUserId(userId: Long): List<Per> {
        return jdbcTemplate.query(
            PerQueries.GET_PER_BY_USER_ID,
            MapSqlParameterSource()
                .addValue("userId", userId)
        ) { rs, _ ->
            PerEntity(
                accountId = rs.getLong("accountId"),
                contractNumber = rs.getString("contractNumber"),
                openingDate = rs.getDate("openingDate").toLocalDate(),
                managementTypeId = rs.getLong("managementTypeId"),
            ).toDomain()
        }
    }
}
