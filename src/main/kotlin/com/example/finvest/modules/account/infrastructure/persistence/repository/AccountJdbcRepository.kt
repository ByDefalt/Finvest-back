package com.example.finvest.modules.account.infrastructure.persistence.repository


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
    private val jdbcTemplate: NamedParameterJdbcTemplate
) : AccountRepository {

    override fun findDashboardByUserId(userId: Long): List<AccountDashboardData> {
        return jdbcTemplate.query(
            AccountQueries.FIND_DASHBOARD_BY_USER_ID,
            MapSqlParameterSource()
                .addValue("userId", userId)
        ) { rs, _ ->
            AccountDashboardRow(
                // Account
                accountId = rs.getLong("account_id"),
                accountBankId = rs.getLong("account_bank_id"),
                accountName = rs.getString("account_name"),
                accountBalance = rs.getBigDecimal("account_balance"),
                accountCurrencyId = rs.getLong("account_currency_id"),
                accountCreatedAt = rs.getTimestamp("account_created_at").toLocalDateTime(),
                accountClosedAt = rs.getTimestamp("account_closed_at")?.toLocalDateTime(),
                accountStatusId = rs.getLong("account_status_id"),
                accountDescription = rs.getString("account_description"),
                accountType = rs.getString("account_type"),

                // Bank
                bankId = rs.getLong("bank_id"),
                bankName = rs.getString("bank_name"),
                bankBic = rs.getString("bank_bic"),
                bankLogo = rs.getString("bank_logo"),

                // Currency
                currencyId = rs.getLong("currency_id"),
                currencyCode = rs.getString("currency_code"),

                // AccountStatus
                statusId = rs.getLong("status_id"),
                statusCode = rs.getString("status_code"),

                // AccountOwner
                ownerId = rs.getLong("owner_id"),
                ownerAccountId = rs.getLong("owner_account_id"),
                ownerUserId = rs.getLong("owner_user_id").let {
                    if (rs.wasNull()) null else it
                },
                ownerName = rs.getString("owner_name"),
                ownerOwnershipPercentage =
                    rs.getBigDecimal("owner_ownership_percentage"),
            ).toDomain()
        }
    }
}
