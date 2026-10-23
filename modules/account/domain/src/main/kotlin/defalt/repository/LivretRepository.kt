package defalt.repository

import defalt.models.Livret
import defalt.valueobject.AccountId
import defalt.valueobject.UserId

/** Port of the persistence of the regulated savings account (livret) details. */
interface LivretRepository {
    /**
     * Details of the accounts of this type owned by the internal user.
     *
     * @param userId id of the internal user.
     * @return the regulated savings account details of the user, an empty list if there is none.
     */
    fun getLivretsByUserId(userId: UserId): List<Livret>

    /**
     * Details of the given accounts, an empty list for no id.
     *
     * @param accountIds ids of the accounts; may be empty.
     * @return the details of these accounts that exist.
     */
    fun getLivretsByAccountIds(accountIds: Set<AccountId>): List<Livret>

    /**
     * Details of the account, or null if it has none.
     *
     * @param accountId id of the account.
     * @return the details, or null if the account has none.
     */
    fun getLivretByAccountId(accountId: AccountId): Livret?

    /**
     * Persists the details of [livret]'s account.
     *
     * @param livret details to persist, with the id of their account.
     */
    fun createLivret(livret: Livret)

    /**
     * Updates the details of [livret]'s account.
     *
     * @param livret new details, with the id of their account.
     */
    fun updateLivret(livret: Livret)

    /**
     * Deletes the details of the account.
     *
     * @param accountId id of the account whose details are deleted.
     */
    fun deleteLivret(accountId: AccountId)
}
