package com.example.finvest.modules.account.infrastructure.persistence.repository

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.domain.models.AssuranceVie
import com.example.finvest.modules.account.domain.repository.AssuranceVieRepository
import com.example.finvest.modules.account.infrastructure.persistence.mapper.toDomain
import com.example.finvest.modules.account.infrastructure.persistence.models.AssuranceVieEntity
import com.example.finvest.modules.account.infrastructure.persistence.queries.AssuranceVieQueries
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class AssuranceVieJdbcRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val logger: Logger
) : AssuranceVieRepository {

    override fun getAssuranceVieByUserId(userId: Long): List<AssuranceVie> {
        return jdbcTemplate.query(
            AssuranceVieQueries.GET_ASSURANCE_VIE_BY_USER_ID,
            MapSqlParameterSource()
                .addValue("userId", userId)
        ) { rs, _ ->
            AssuranceVieEntity(
                accountId = rs.getLong("accountId"),
                contractNumber = rs.getString("contractNumber"),
                openingDate = rs.getDate("openingDate").toLocalDate(),
                managementTypeId = rs.getLong("managementTypeId")
            ).toDomain()
        }
    }
}
