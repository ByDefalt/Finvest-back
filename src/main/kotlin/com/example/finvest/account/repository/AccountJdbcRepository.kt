package com.example.finvest.account.repository

import com.example.finvest.account.entity.AccountDashboardRow
import com.example.finvest.account.entity.AccountEntity
import com.example.finvest.account.exeption.AccountNotCreatedException
import com.example.finvest.account.exeption.AccountNotDeleteException
import com.example.finvest.account.exeption.AccountNotUpdateException
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository

@Repository
class AccountJdbcRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) {

    fun findAllByUserId(userId: Long): List<AccountEntity> {
        return jdbcTemplate.query(
            AccountQueries.FIND_ALL_BY_USER_ID,
            MapSqlParameterSource("userId", userId)
        ) { rs, _ ->
            AccountEntity(
                id = rs.getLong("id"),
                bankId = rs.getObject("bank_id", Long::class.java),
                name = rs.getString("name"),
                balance = rs.getBigDecimal("balance"),
                currencyId = rs.getLong("currency_id"),
                createdAt = rs.getTimestamp("created_at").toLocalDateTime(),
                closedAt = rs.getTimestamp("closed_at")?.toLocalDateTime(),
                accountStatusId = rs.getLong("account_status_id"),
                description = rs.getString("description"),
                accountTypeId = rs.getLong("account_type_id")
            )
        }
    }

    fun deleteByIdAndUserId(id: Long, userId: Long) {
        val affectedRows = jdbcTemplate.update(
            AccountQueries.DELETE_BY_ID_AND_USER_ID,
            MapSqlParameterSource()
                .addValue("id", id)
                .addValue("userId", userId)
        )

        if (affectedRows == 0) {
            throw AccountNotDeleteException()
        }
    }

    fun insertAccount(account: AccountEntity): AccountEntity {
        val keyHolder = GeneratedKeyHolder()

        val params = MapSqlParameterSource()
            .addValue("bankId", account.bankId)
            .addValue("name", account.name)
            .addValue("balance", account.balance)
            .addValue("currencyId", account.currencyId)
            .addValue("accountStatusId", account.accountStatusId)
            .addValue("description", account.description)

        val affectedRows = jdbcTemplate.update(
            AccountQueries.INSERT_ACCOUNT,
            params,
            keyHolder,
            arrayOf("id")
        )

        if (affectedRows == 0) {
            throw AccountNotCreatedException()
        }

        val id = keyHolder.key?.toLong()
            ?: throw IllegalStateException("Failed to retrieve generated account ID")

        return account.copy(id = id)
    }

    fun updateAccount(
        account: AccountEntity,
        userId: Long
    ): AccountEntity {

        val affectedRows = jdbcTemplate.update(
            AccountQueries.UPDATE_ACCOUNT,
            MapSqlParameterSource()
                .addValue("id", account.id)
                .addValue("bankId", account.bankId)
                .addValue("name", account.name)
                .addValue("balance", account.balance)
                .addValue("currencyId", account.currencyId)
                .addValue("accountStatusId", account.accountStatusId)
                .addValue("description", account.description)
                .addValue("userId", userId)
        )

        if (affectedRows == 0) {
            throw AccountNotUpdateException()
        }

        return account
    }

    fun findByIdAndUserId(id: Long, userId: Long): AccountEntity {
        return jdbcTemplate.query(
            AccountQueries.FIND_BY_ID_AND_USER_ID,
            MapSqlParameterSource()
                .addValue("id", id)
                .addValue("userId", userId)
        ) { rs, _ ->
            AccountEntity(
                id = rs.getLong("id"),
                bankId = rs.getObject("bank_id", Long::class.java),
                name = rs.getString("name"),
                balance = rs.getBigDecimal("balance"),
                currencyId = rs.getLong("currency_id"),
                createdAt = rs.getTimestamp("created_at").toLocalDateTime(),
                closedAt = rs.getTimestamp("closed_at")?.toLocalDateTime(),
                accountStatusId = rs.getLong("account_status_id"),
                accountTypeId = rs.getLong("account_type_id"),
                description = rs.getString("description")
            )
        }.firstOrNull() ?: throw AccountNotUpdateException()
    }

    fun findDashboardByUserId(userId: Long): List<AccountDashboardRow> {
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
                currencyName = rs.getString("currency_name"),
                currencySymbol = rs.getString("currency_symbol"),

                // AccountStatus
                statusId = rs.getLong("status_id"),
                statusCode = rs.getString("status_code"),
                statusName = rs.getString("status_name"),

                // AccountOwner
                ownerId = rs.getLong("owner_id"),
                ownerAccountId = rs.getLong("owner_account_id"),
                ownerUserId = rs.getLong("owner_user_id").let {
                    if (rs.wasNull()) null else it
                },
                ownerName = rs.getString("owner_name"),
                ownerOwnershipPercentage =
                    rs.getBigDecimal("owner_ownership_percentage")
            )
        }
    }
}
