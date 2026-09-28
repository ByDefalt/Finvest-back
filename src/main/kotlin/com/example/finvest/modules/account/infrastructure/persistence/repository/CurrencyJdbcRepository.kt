package com.example.finvest.modules.account.infrastructure.persistence.repository

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.domain.repository.CurrencyRepository
import com.example.finvest.modules.account.domain.valueobject.CurrencyCode
import com.example.finvest.modules.account.infrastructure.persistence.queries.CurrencyQueries
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class CurrencyJdbcRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val logger: Logger,
) : CurrencyRepository {
    override fun getCurrencies(): Map<Long, CurrencyCode> {
        logger.info("Fetching currencies from database")
        return jdbcTemplate
            .query(CurrencyQueries.GET_CURRENCIES) { rs, _ ->
                rs.getLong("id") to CurrencyCode(rs.getString("code"))
            }.toMap()
    }
}
