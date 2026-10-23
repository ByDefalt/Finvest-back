package defalt.repository

import defalt.models.Per
import defalt.valueobject.AccountId
import defalt.valueobject.UserId

/** Port of the persistence of the PER (plan d'épargne retraite, retirement savings plan) details. */
interface PerRepository {
    /**
     * Details of the accounts of this type owned by the internal user.
     *
     * @param userId id of the internal user.
     * @return the PER details of the user, an empty list if there is none.
     */
    fun getPersByUserId(userId: UserId): List<Per>

    /**
     * Details of the given accounts, an empty list for no id.
     *
     * @param accountIds ids of the accounts; may be empty.
     * @return the details of these accounts that exist.
     */
    fun getPersByAccountIds(accountIds: Set<AccountId>): List<Per>

    /**
     * Details of the account, or null if it has none.
     *
     * @param accountId id of the account.
     * @return the details, or null if the account has none.
     */
    fun getPerByAccountId(accountId: AccountId): Per?

    /**
     * Persists the details of [per]'s account.
     *
     * @param per details to persist, with the id of their account.
     */
    fun createPer(per: Per)

    /**
     * Updates the details of [per]'s account.
     *
     * @param per new details, with the id of their account.
     */
    fun updatePer(per: Per)

    /**
     * Deletes the details of the account.
     *
     * @param accountId id of the account whose details are deleted.
     */
    fun deletePer(accountId: AccountId)
}
