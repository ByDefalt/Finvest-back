package defalt.repository

import defalt.models.Pee
import defalt.valueobject.AccountId
import defalt.valueobject.UserId

/** Port of the persistence of the PEE (plan d'épargne entreprise, employee savings plan) details. */
interface PeeRepository {
    /**
     * Details of the accounts of this type owned by the internal user.
     *
     * @param userId id of the internal user.
     * @return the PEE details of the user, an empty list if there is none.
     */
    fun getPeesByUserId(userId: UserId): List<Pee>

    /**
     * Details of the given accounts, an empty list for no id.
     *
     * @param accountIds ids of the accounts; may be empty.
     * @return the details of these accounts that exist.
     */
    fun getPeesByAccountIds(accountIds: Set<AccountId>): List<Pee>

    /**
     * Details of the account, or null if it has none.
     *
     * @param accountId id of the account.
     * @return the details, or null if the account has none.
     */
    fun getPeeByAccountId(accountId: AccountId): Pee?

    /**
     * Persists the details of [pee]'s account.
     *
     * @param pee details to persist, with the id of their account.
     */
    fun createPee(pee: Pee)

    /**
     * Updates the details of [pee]'s account.
     *
     * @param pee new details, with the id of their account.
     */
    fun updatePee(pee: Pee)

    /**
     * Deletes the details of the account.
     *
     * @param accountId id of the account whose details are deleted.
     */
    fun deletePee(accountId: AccountId)
}
