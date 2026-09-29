package com.example.finvest.modules.account.infrastructure.persistence.repository

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.domain.cache.ReferenceDataCache
import com.example.finvest.modules.account.domain.models.Account
import com.example.finvest.modules.account.domain.repository.AccountRepository
import com.example.finvest.modules.account.domain.valueobject.AccountId
import com.example.finvest.modules.account.infrastructure.persistence.mapper.toAccountId
import com.example.finvest.modules.account.infrastructure.persistence.mapper.toDomain
import com.example.finvest.modules.account.infrastructure.persistence.models.AccountEntity
import com.example.finvest.modules.account.infrastructure.persistence.queries.AccountQueries
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository

@Repository
class AccountJdbcRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val referenceDataCache: ReferenceDataCache,
    private val logger: Logger,
) : AccountRepository {
    override fun getAccountByUserId(userId: Long): List<Account> {
        logger.info("Fetching accounts for userId: $userId")
        return jdbcTemplate.query(
            AccountQueries.GET_ACCOUNT_BY_USER_ID,
            MapSqlParameterSource()
                .addValue("userId", userId),
        ) { rs, _ ->
            AccountEntity(
                id = rs.getLong("accountId"),
                bankId = rs.getLong("bankId"),
                name = rs.getString("name"),
                balance = rs.getBigDecimal("balance"),
                currencyId = rs.getLong("currencyId"),
                createdAt = rs.getTimestamp("createdAt").toLocalDateTime(),
                closedAt = rs.getTimestamp("closedAt")?.toLocalDateTime(),
                accountStatusId = rs.getLong("accountStatusId"),
                description = rs.getString("description"),
                accountTypeId = rs.getLong("accountTypeId"),
            ).toDomain(referenceDataCache)
        }
    }

    override fun createAccount(account: Account): AccountId {
        val keyHolder = GeneratedKeyHolder()
        logger.info("Creating account with name: ${account.name}")
        jdbcTemplate.update(
            AccountQueries.CREATE_ACCOUNT,
            MapSqlParameterSource()
                .addValue("bankId", account.bank.id.value)
                .addValue("name", account.name)
                .addValue("balance", account.balance.amount)
                .addValue("currencyId", referenceDataCache.currencyIdOf(account.balance.currencyCode.value))
                .addValue("createdAt", account.createdAt)
                .addValue("closedAt", account.closedAt)
                .addValue("accountStatusId", referenceDataCache.statusIdOf(account.accountStatus.name))
                .addValue("description", account.description)
                .addValue("accountTypeId", referenceDataCache.typeIdOf(account.accountType.value)),
            keyHolder,
            arrayOf("id"),
        )
        logger.info("Account created with id: ${keyHolder.key}")
        return keyHolder.key!!.toLong().toAccountId()
    }

    override fun updateAccount(account: Account): Account {
        logger.info("Updating account with id: ${account.id.value}")
        jdbcTemplate.update(
            AccountQueries.UPDATE_ACCOUNT,
            MapSqlParameterSource()
                .addValue("accountId", account.id.value)
                .addValue("bankId", account.bank.id.value)
                .addValue("name", account.name)
                .addValue("balance", account.balance.amount)
                .addValue("currencyId", referenceDataCache.currencyIdOf(account.balance.currencyCode.value))
                .addValue("createdAt", account.createdAt)
                .addValue("closedAt", account.closedAt)
                .addValue("accountStatusId", referenceDataCache.statusIdOf(account.accountStatus.name))
                .addValue("description", account.description)
                .addValue("accountTypeId", referenceDataCache.typeIdOf(account.accountType.value)),
        )
        logger.info("Account updated with id: ${account.id.value}")
        return account
    }

    override fun deleteAccount(accountId: AccountId) {
        logger.info("Deleting account with id: ${accountId.value}")
        jdbcTemplate.update(
            AccountQueries.DELETE_ACCOUNT,
            MapSqlParameterSource()
                .addValue("accountId", accountId.value),
        )
        logger.info("Account deleted with id: ${accountId.value}")
    }
}
