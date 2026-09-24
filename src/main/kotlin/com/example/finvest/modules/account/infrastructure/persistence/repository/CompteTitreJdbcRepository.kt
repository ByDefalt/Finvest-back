package com.example.finvest.modules.account.infrastructure.persistence.repository

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.domain.models.CompteTitre
import com.example.finvest.modules.account.domain.repository.CompteTitreRepository
import com.example.finvest.modules.account.infrastructure.persistence.mapper.toDomain
import com.example.finvest.modules.account.infrastructure.persistence.models.CompteTitreEntity
import com.example.finvest.modules.account.infrastructure.persistence.queries.CompteTitreQueries
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class CompteTitreJdbcRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val logger: Logger
) : CompteTitreRepository {

    override fun getCompteTitreByUserId(userId: Long): List<CompteTitre> {
        return jdbcTemplate.query(
            CompteTitreQueries.GET_COMPTE_TITRE_BY_USER_ID,
            MapSqlParameterSource()
                .addValue("userId", userId)
        ) { rs, _ ->
            CompteTitreEntity(
                accountId = rs.getLong("accountId"),
                accountNumber = rs.getString("accountNumber"),
            ).toDomain()

        }
    }
}
