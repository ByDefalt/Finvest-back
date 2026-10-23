package defalt.persistence.repository

import defalt.exception.CommonErrorCode
import defalt.exception.CustomException
import defalt.logger.Logger
import defalt.models.Account
import defalt.models.AccountWithBank
import defalt.models.Bank
import defalt.persistence.queries.AccountQueries
import defalt.repository.AccountRepository
import defalt.valueobject.AccountId
import defalt.valueobject.AccountStatusCode
import defalt.valueobject.AccountTypeCode
import defalt.valueobject.BankBic
import defalt.valueobject.CurrencyCode
import defalt.valueobject.UserId
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository

/**
 * JDBC adapter of the [AccountRepository] port, on the `accounts` and `banks` tables.
 *
 * @param jdbcTemplate runs the SQL queries.
 * @param logger logger of the class.
 */
@Repository
class AccountJdbcRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val logger: Logger,
) : AccountRepository {
    /**
     * Accounts owned by the internal user.
     *
     * @param userId id of the internal user.
     * @return the accounts owned by the user, an empty list if there is none.
     */
    override fun getAccountsByUserId(userId: UserId): List<Account> =
        jdbcTemplate.query(
            AccountQueries.GET_BY_USER_ID,
            MapSqlParameterSource("userId", userId.value),
        ) { rs, _ ->
            Account(
                id = AccountId(rs.getLong("id")),
                bankBic = BankBic(rs.getString("bank_bic")),
                name = rs.getString("name"),
                balance = rs.getBigDecimal("balance"),
                currencyCode = CurrencyCode(rs.getString("currency_code")),
                createdAt = rs.getTimestamp("created_at").toLocalDateTime(),
                closedAt = rs.getTimestamp("closed_at")?.toLocalDateTime(),
                accountStatusCode = AccountStatusCode(rs.getString("account_status_code")),
                description = rs.getString("description"),
                accountTypeCode = AccountTypeCode(rs.getString("account_type_code")),
            )
        }

    /**
     * Accounts with the given ids, an empty list for no id.
     *
     * @param accountIds ids of the accounts; may be empty.
     * @return the existing accounts among these ids.
     */
    override fun getAccountsByAccountIds(accountIds: Set<AccountId>): List<Account> =
        if (accountIds.isEmpty()) {
            emptyList()
        } else {
            jdbcTemplate.query(
                AccountQueries.GET_BY_ACCOUNT_IDS,
                MapSqlParameterSource("accountIds", accountIds.map(AccountId::value)),
            ) { rs, _ ->
                Account(
                    id = AccountId(rs.getLong("id")),
                    bankBic = BankBic(rs.getString("bank_bic")),
                    name = rs.getString("name"),
                    balance = rs.getBigDecimal("balance"),
                    currencyCode = CurrencyCode(rs.getString("currency_code")),
                    createdAt = rs.getTimestamp("created_at").toLocalDateTime(),
                    closedAt = rs.getTimestamp("closed_at")?.toLocalDateTime(),
                    accountStatusCode = AccountStatusCode(rs.getString("account_status_code")),
                    description = rs.getString("description"),
                    accountTypeCode = AccountTypeCode(rs.getString("account_type_code")),
                )
            }
        }

    /**
     * Accounts owned by the internal user, with their bank.
     *
     * @param userId id of the internal user.
     * @return the accounts owned by the user with their bank, an empty list if there is none.
     */
    override fun getAccountsWithBanksByUserId(userId: UserId): List<AccountWithBank> =
        jdbcTemplate.query(
            AccountQueries.GET_ACCOUNTS_WITH_BANKS_BY_USER_ID,
            MapSqlParameterSource("userId", userId.value),
        ) { rs, _ ->
            AccountWithBank(
                account =
                    Account(
                        id = AccountId(rs.getLong("id")),
                        bankBic = BankBic(rs.getString("bank_bic")),
                        name = rs.getString("name"),
                        balance = rs.getBigDecimal("balance"),
                        currencyCode = CurrencyCode(rs.getString("currency_code")),
                        createdAt = rs.getTimestamp("created_at").toLocalDateTime(),
                        closedAt = rs.getTimestamp("closed_at")?.toLocalDateTime(),
                        accountStatusCode = AccountStatusCode(rs.getString("account_status_code")),
                        description = rs.getString("description"),
                        accountTypeCode = AccountTypeCode(rs.getString("account_type_code")),
                    ),
                bank =
                    Bank(
                        bic = BankBic(rs.getString("bank_bic")),
                        name = rs.getString("bank_name"),
                        logo = rs.getString("bank_logo"),
                    ),
            )
        }

    /**
     * The account, or null if it does not exist.
     *
     * @param accountId id of the account.
     * @return the account, or null if it does not exist.
     */
    override fun getAccountByAccountId(accountId: AccountId): Account? =
        jdbcTemplate
            .query(
                AccountQueries.GET_BY_ID,
                MapSqlParameterSource("accountId", accountId.value),
            ) { rs, _ ->
                Account(
                    id = AccountId(rs.getLong("id")),
                    bankBic = BankBic(rs.getString("bank_bic")),
                    name = rs.getString("name"),
                    balance = rs.getBigDecimal("balance"),
                    currencyCode = CurrencyCode(rs.getString("currency_code")),
                    createdAt = rs.getTimestamp("created_at").toLocalDateTime(),
                    closedAt = rs.getTimestamp("closed_at")?.toLocalDateTime(),
                    accountStatusCode = AccountStatusCode(rs.getString("account_status_code")),
                    description = rs.getString("description"),
                    accountTypeCode = AccountTypeCode(rs.getString("account_type_code")),
                )
            }.firstOrNull()

    /**
     * Persists a new account; its id is ignored.
     *
     * @param account account to persist.
     * @return the generated id.
     */
    override fun createAccount(account: Account): AccountId {
        logger.debug("Creating account in database: name=${account.name}, type=${account.accountTypeCode.value}")
        val keyHolder = GeneratedKeyHolder()

        jdbcTemplate.update(
            AccountQueries.INSERT,
            MapSqlParameterSource()
                .addValue("bankBic", account.bankBic.value)
                .addValue("name", account.name)
                .addValue("balance", account.balance)
                .addValue("currencyCode", account.currencyCode.value)
                .addValue("createdAt", account.createdAt)
                .addValue("closedAt", account.closedAt)
                .addValue("accountStatusCode", account.accountStatusCode.value)
                .addValue("description", account.description)
                .addValue("accountTypeCode", account.accountTypeCode.value),
            keyHolder,
            arrayOf("id"),
        )

        return keyHolder.key?.toLong()?.let(::AccountId)?.also {
            logger.debug("Account inserted in database: accountId=${it.value}")
        } ?: throw CustomException(CommonErrorCode.INTERNAL_ERROR, "Account creation did not return a generated id")
    }

    /**
     * Updates the editable fields of the account. Its type and creation date are kept.
     *
     * @param account account to update, with its id.
     */
    override fun updateAccount(account: Account) {
        logger.debug("Updating account in database: accountId=${account.id.value}")
        jdbcTemplate.update(
            AccountQueries.UPDATE,
            MapSqlParameterSource()
                .addValue("accountId", account.id.value)
                .addValue("bankBic", account.bankBic.value)
                .addValue("name", account.name)
                .addValue("balance", account.balance)
                .addValue("currencyCode", account.currencyCode.value)
                .addValue("closedAt", account.closedAt)
                .addValue("accountStatusCode", account.accountStatusCode.value)
                .addValue("description", account.description),
        )
    }

    /**
     * Deletes the account, whose details and owners must have been deleted first.
     *
     * @param accountId id of the account to delete.
     */
    override fun deleteAccount(accountId: AccountId) {
        logger.debug("Deleting account in database: accountId=${accountId.value}")
        jdbcTemplate.update(AccountQueries.DELETE, MapSqlParameterSource("accountId", accountId.value))
    }
}
