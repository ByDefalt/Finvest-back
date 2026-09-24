package com.example.finvest.modules.account.infrastructure.persistence.repository


import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.domain.models.AccountDashboardData
import com.example.finvest.modules.account.domain.repository.AccountRepository
import com.example.finvest.modules.account.infrastructure.persistence.mapper.toDomain
import com.example.finvest.modules.account.infrastructure.persistence.models.AccountDashboardRow
import com.example.finvest.modules.account.infrastructure.persistence.queries.AccountQueries
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class AccountJdbcRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val logger: Logger
) : AccountRepository {

    override fun findDashboardByUserId(userId: Long): List<AccountDashboardData> {
        return jdbcTemplate.query(
            AccountQueries.FIND_DASHBOARD_BY_USER_ID,
            MapSqlParameterSource()
                .addValue("userId", userId)
        ) { rs, _ ->
            AccountDashboardRow(
                // Account
                accountId = rs.getLong("accountId"),
                accountBankId = rs.getLong("accountBankId"),
                accountName = rs.getString("accountName"),
                accountBalance = rs.getBigDecimal("accountBalance"),
                accountCurrencyId = rs.getLong("accountCurrencyId"),
                accountCreatedAt = rs.getTimestamp("accountCreatedAt").toLocalDateTime(),
                accountClosedAt = rs.getTimestamp("accountClosedAt")?.toLocalDateTime(),
                accountStatusId = rs.getLong("accountStatusId"),
                accountDescription = rs.getString("accountDescription"),
                accountType = rs.getString("accountType"),

                // Bank
                bankId = rs.getLong("bankId"),
                bankName = rs.getString("bankName"),
                bankBic = rs.getString("bankBic"),
                bankLogo = rs.getString("bankLogo"),

                // Currency
                currencyId = rs.getLong("currencyId"),
                currencyCode = rs.getString("currencyCode"),

                // AccountStatus
                statusId = rs.getLong("statusId"),
                statusCode = rs.getString("statusCode"),

                // AccountOwner
                ownerId = rs.getLong("ownerId"),
                ownerAccountId = rs.getLong("ownerAccountId"),
                ownerUserId = rs.getLong("ownerUserId").let {
                    if (rs.wasNull()) null else it
                },
                ownerName = rs.getString("ownerName"),
                ownerOwnershipPercentage =
                    rs.getBigDecimal("ownerOwnershipPercentage"),
            ).toDomain()
        }
    }
}
