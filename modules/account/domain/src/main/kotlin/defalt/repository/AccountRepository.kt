package defalt.repository

import defalt.models.Account
import defalt.models.AccountWithBank
import defalt.valueobject.AccountId
import defalt.valueobject.UserId

/** Port of the persistence of the accounts. */
interface AccountRepository {
    /**
     * Accounts owned by the internal user.
     *
     * @param userId id of the internal user.
     * @return the accounts owned by the user, an empty list if there is none.
     */
    fun getAccountsByUserId(userId: UserId): List<Account>

    /**
     * Accounts with the given ids, an empty list for no id.
     *
     * @param accountIds ids of the accounts; may be empty.
     * @return the existing accounts among these ids.
     */
    fun getAccountsByAccountIds(accountIds: Set<AccountId>): List<Account>

    /**
     * Accounts owned by the internal user, with their bank.
     *
     * @param userId id of the internal user.
     * @return the accounts owned by the user with their bank, an empty list if there is none.
     */
    fun getAccountsWithBanksByUserId(userId: UserId): List<AccountWithBank>

    /**
     * The account, or null if it does not exist.
     *
     * @param accountId id of the account.
     * @return the account, or null if it does not exist.
     */
    fun getAccountByAccountId(accountId: AccountId): Account?

    /**
     * Persists a new account; its id is ignored.
     *
     * @param account account to persist.
     * @return the generated id.
     */
    fun createAccount(account: Account): AccountId

    /**
     * Updates the editable fields of the account. Its type and creation date are kept.
     *
     * @param account account to update, with its id.
     */
    fun updateAccount(account: Account)

    /**
     * Deletes the account, whose details and owners must have been deleted first.
     *
     * @param accountId id of the account to delete.
     */
    fun deleteAccount(accountId: AccountId)
}
