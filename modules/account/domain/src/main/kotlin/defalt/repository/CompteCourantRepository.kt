package defalt.repository

import defalt.models.CompteCourant
import defalt.valueobject.AccountId
import defalt.valueobject.UserId

/** Port of the persistence of the current account details. */
interface CompteCourantRepository {
    /**
     * Details of the accounts of this type owned by the internal user.
     *
     * @param userId id of the internal user.
     * @return the current account details of the user, an empty list if there is none.
     */
    fun getComptesCourantsByUserId(userId: UserId): List<CompteCourant>

    /**
     * Details of the given accounts, an empty list for no id.
     *
     * @param accountIds ids of the accounts; may be empty.
     * @return the details of these accounts that exist.
     */
    fun getComptesCourantsByAccountIds(accountIds: Set<AccountId>): List<CompteCourant>

    /**
     * Details of the account, or null if it has none.
     *
     * @param accountId id of the account.
     * @return the details, or null if the account has none.
     */
    fun getCompteCourantByAccountId(accountId: AccountId): CompteCourant?

    /**
     * Persists the details of [compteCourant]'s account.
     *
     * @param compteCourant details to persist, with the id of their account.
     */
    fun createCompteCourant(compteCourant: CompteCourant)

    /**
     * Updates the details of [compteCourant]'s account.
     *
     * @param compteCourant new details, with the id of their account.
     */
    fun updateCompteCourant(compteCourant: CompteCourant)

    /**
     * Deletes the details of the account.
     *
     * @param accountId id of the account whose details are deleted.
     */
    fun deleteCompteCourant(accountId: AccountId)
}
