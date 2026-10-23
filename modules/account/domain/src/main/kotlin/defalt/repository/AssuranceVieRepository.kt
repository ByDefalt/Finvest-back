package defalt.repository

import defalt.models.AssuranceVie
import defalt.valueobject.AccountId
import defalt.valueobject.UserId

/** Port of the persistence of the life insurance contract (assurance vie) details. */
interface AssuranceVieRepository {
    /**
     * Details of the accounts of this type owned by the internal user.
     *
     * @param userId id of the internal user.
     * @return the life insurance contract details of the user, an empty list if there is none.
     */
    fun getAssuranceViesByUserId(userId: UserId): List<AssuranceVie>

    /**
     * Details of the given accounts, an empty list for no id.
     *
     * @param accountIds ids of the accounts; may be empty.
     * @return the details of these accounts that exist.
     */
    fun getAssuranceViesByAccountIds(accountIds: Set<AccountId>): List<AssuranceVie>

    /**
     * Details of the account, or null if it has none.
     *
     * @param accountId id of the account.
     * @return the details, or null if the account has none.
     */
    fun getAssuranceVieByAccountId(accountId: AccountId): AssuranceVie?

    /**
     * Persists the details of [assuranceVie]'s account.
     *
     * @param assuranceVie details to persist, with the id of their account.
     */
    fun createAssuranceVie(assuranceVie: AssuranceVie)

    /**
     * Updates the details of [assuranceVie]'s account.
     *
     * @param assuranceVie new details, with the id of their account.
     */
    fun updateAssuranceVie(assuranceVie: AssuranceVie)

    /**
     * Deletes the details of the account.
     *
     * @param accountId id of the account whose details are deleted.
     */
    fun deleteAssuranceVie(accountId: AccountId)
}
