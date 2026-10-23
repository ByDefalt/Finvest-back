package defalt.repository

import defalt.models.AccountOwner
import defalt.valueobject.AccountId
import defalt.valueobject.AccountOwnerId

/** Port of the persistence of the account owners. */
interface AccountOwnerRepository {
    /**
     * Owners of the given accounts, an empty list for no account.
     *
     * @param accountIds ids of the accounts; may be empty.
     * @return the owners of these accounts, an empty list if there is none.
     */
    fun getAccountOwnersByAccountIds(accountIds: Set<AccountId>): List<AccountOwner>

    /**
     * Persists the owners, whose [defalt.valueobject.OwnerRef] must be resolved (internal or external user).
     *
     * @param owners owners to persist, with a resolved owner reference.
     * @return the generated ids, in the order of [owners].
     */
    fun createAccountOwners(owners: List<AccountOwner>): List<AccountOwnerId>

    /**
     * Deletes every owner of the account.
     *
     * @param accountId id of the account whose owners are deleted.
     */
    fun deleteAccountOwnersByAccountId(accountId: AccountId)
}
