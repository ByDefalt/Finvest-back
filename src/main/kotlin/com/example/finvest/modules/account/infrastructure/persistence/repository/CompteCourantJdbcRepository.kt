package com.example.finvest.modules.account.infrastructure.persistence.repository

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.domain.models.CompteCourant
import com.example.finvest.modules.account.domain.repository.CompteCourantRepository
import com.example.finvest.modules.account.infrastructure.persistence.mapper.toDomain
import com.example.finvest.modules.account.infrastructure.persistence.models.CompteCourantEntity
import com.example.finvest.modules.account.infrastructure.persistence.queries.CompteCourantQueries
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class CompteCourantJdbcRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val logger: Logger
) : CompteCourantRepository {

    override fun getCompteCourantByUserId(userId: Long): List<CompteCourant> {
        return jdbcTemplate.query(
            CompteCourantQueries.GET_COMPTES_COURANTS_BY_USER_ID,
            MapSqlParameterSource()
                .addValue("userId", userId)
        ) { rs, _ ->
            CompteCourantEntity(
                accountId = rs.getLong("accountId"),
                iban = rs.getString("iban"),
                bic = rs.getString("bic"),
                accountNumber = rs.getString("accountNumber"),
                overdraftLimit = rs.getBigDecimal("overdraftLimit"),
                holderName = rs.getString("holderName")
            ).toDomain()
        }
    }
}