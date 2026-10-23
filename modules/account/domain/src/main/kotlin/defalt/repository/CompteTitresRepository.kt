package defalt.repository

import defalt.models.CompteTitres
import defalt.valueobject.AccountId
import defalt.valueobject.UserId

/** Port of the persistence of the securities account (compte-titres) details. */
interface CompteTitresRepository {
    /**
     * Details of the accounts of this type owned by the internal user.
     *
     * @param userId id of the internal user.
     * @return the securities account details of the user, an empty list if there is none.
     */
    fun getComptesTitresByUserId(userId: UserId): List<CompteTitres>

    /**
     * Details of the given accounts, an empty list for no id.
     *
     * @param accountIds ids of the accounts; may be empty.
     * @return the details of these accounts that exist.
     */
    fun getComptesTitresByAccountIds(accountIds: Set<AccountId>): List<CompteTitres>

    /**
     * Details of the account, or null if it has none.
     *
     * @param accountId id of the account.
     * @return the details, or null if the account has none.
     */
    fun getCompteTitresByAccountId(accountId: AccountId): CompteTitres?

    /**
     * Persists the details of [compteTitres]'s account.
     *
     * @param compteTitres details to persist, with the id of their account.
     */
    fun createCompteTitres(compteTitres: CompteTitres)

    /**
     * Updates the details of [compteTitres]'s account.
     *
     * @param compteTitres new details, with the id of their account.
     */
    fun updateCompteTitres(compteTitres: CompteTitres)

    /**
     * Deletes the details of the account.
     *
     * @param accountId id of the account whose details are deleted.
     */
    fun deleteCompteTitres(accountId: AccountId)
}
