package com.example.finvest.modules.account.infrastructure.persistence.repository

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.domain.repository.AccountTypeRepository
import com.example.finvest.modules.account.domain.valueobject.AccountType
import com.example.finvest.modules.account.infrastructure.persistence.queries.AccountTypeQueries
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class AccountTypeJdbcRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val logger: Logger,
) : AccountTypeRepository {
    override fun getAccountTypes(): Map<Long, AccountType> {
        logger.info("Fetching account types from database")
        return jdbcTemplate
            .query(AccountTypeQueries.GET_ACCOUNT_TYPES) { rs, _ ->
                rs.getLong("id") to AccountType(rs.getString("code"))
            }.toMap()
    }
}
