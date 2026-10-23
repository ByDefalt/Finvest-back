package defalt.fakes

import defalt.fixtures.AccountFixtures
import defalt.models.Account
import defalt.models.AccountOwner
import defalt.models.AccountWithBank
import defalt.repository.AccountOwnerRepository
import defalt.repository.AccountRepository
import defalt.valueobject.AccountId
import defalt.valueobject.AccountOwnerId
import defalt.valueobject.OwnerRef
import defalt.valueobject.UserId

class InMemoryAccountOwnerRepository : AccountOwnerRepository {
    private var nextId = 1L
    val owners = mutableListOf<AccountOwner>()

    fun seed(vararg owners: AccountOwner) = apply { this.owners += owners }

    fun ownersOf(accountId: AccountId): List<AccountOwner> = owners.filter { it.accountId == accountId }

    override fun getAccountOwnersByAccountIds(accountIds: Set<AccountId>): List<AccountOwner> = owners.filter { it.accountId in accountIds }

    override fun createAccountOwners(owners: List<AccountOwner>): List<AccountOwnerId> =
        owners.map { owner ->
            require(owner.owner !is OwnerRef.Unknown) { "Cannot persist an unresolved owner" }
            val id = AccountOwnerId(nextId++)
            this.owners += owner.copy(id = id)
            id
        }

    override fun deleteAccountOwnersByAccountId(accountId: AccountId) {
        owners.removeAll { it.accountId == accountId }
    }
}

class InMemoryAccountRepository(
    private val ownerRepository: InMemoryAccountOwnerRepository,
) : AccountRepository {
    private var nextId = 100L
    val accounts = linkedMapOf<AccountId, Account>()
    val updated = mutableListOf<Account>()

    fun seed(vararg accounts: Account) = apply { accounts.forEach { this.accounts[it.id] = it } }

    override fun getAccountsByUserId(userId: UserId): List<Account> = accounts.values.filter { isOwnedBy(it.id, userId) }

    override fun getAccountsByAccountIds(accountIds: Set<AccountId>): List<Account> = accounts.values.filter { it.id in accountIds }

    override fun getAccountsWithBanksByUserId(userId: UserId): List<AccountWithBank> =
        getAccountsByUserId(userId).map { AccountFixtures.accountWithBank(it) }

    override fun getAccountByAccountId(accountId: AccountId): Account? = accounts[accountId]

    override fun createAccount(account: Account): AccountId {
        val id = AccountId(nextId++)
        accounts[id] = account.copy(id = id)
        return id
    }

    override fun updateAccount(account: Account) {
        updated += account
        accounts[account.id] = account
    }

    override fun deleteAccount(accountId: AccountId) {
        accounts.remove(accountId)
    }

    private fun isOwnedBy(
        accountId: AccountId,
        userId: UserId,
    ) = ownerRepository.ownersOf(accountId).any { it.owner == OwnerRef.Internal(userId) }
}
