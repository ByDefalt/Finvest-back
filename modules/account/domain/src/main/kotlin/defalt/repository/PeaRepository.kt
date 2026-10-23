package defalt.repository

import defalt.models.Pea
import defalt.valueobject.AccountId
import defalt.valueobject.UserId

/** Port of the persistence of the PEA (plan d'épargne en actions, French equity savings plan) details. */
interface PeaRepository {
    /**
     * Details of the accounts of this type owned by the internal user.
     *
     * @param userId id of the internal user.
     * @return the PEA details of the user, an empty list if there is none.
     */
    fun getPeasByUserId(userId: UserId): List<Pea>

    /**
     * Details of the given accounts, an empty list for no id.
     *
     * @param accountIds ids of the accounts; may be empty.
     * @return the details of these accounts that exist.
     */
    fun getPeasByAccountIds(accountIds: Set<AccountId>): List<Pea>

    /**
     * Details of the account, or null if it has none.
     *
     * @param accountId id of the account.
     * @return the details, or null if the account has none.
     */
    fun getPeaByAccountId(accountId: AccountId): Pea?

    /**
     * Persists the details of [pea]'s account.
     *
     * @param pea details to persist, with the id of their account.
     */
    fun createPea(pea: Pea)

    /**
     * Updates the details of [pea]'s account.
     *
     * @param pea new details, with the id of their account.
     */
    fun updatePea(pea: Pea)

    /**
     * Deletes the details of the account.
     *
     * @param accountId id of the account whose details are deleted.
     */
    fun deletePea(accountId: AccountId)
}
