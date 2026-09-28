package com.example.finvest.modules.account.infrastructure.persistence.repository

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.domain.models.AccountStatus
import com.example.finvest.modules.account.domain.repository.AccountStatusRepository
import com.example.finvest.modules.account.infrastructure.persistence.queries.AccountStatusQueries
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class AccountStatusJdbcRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val logger: Logger,
) : AccountStatusRepository {
    override fun getAccountStatuses(): Map<Long, AccountStatus> {
        logger.info("Fetching account statuses from database")
        return jdbcTemplate
            .query(AccountStatusQueries.GET_ACCOUNT_STATUSES) { rs, _ ->
                rs.getLong("id") to AccountStatus.valueOf(rs.getString("code"))
            }.toMap()
    }
}
