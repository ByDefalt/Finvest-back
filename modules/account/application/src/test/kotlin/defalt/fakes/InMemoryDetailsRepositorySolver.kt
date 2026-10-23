package defalt.fakes

import defalt.models.Account
import defalt.models.AccountDetails
import defalt.solver.DetailsRepositorySolver
import defalt.valueobject.AccountId
import defalt.valueobject.AccountTypeCode

/** Stores the details of a single account type, keyed by the account id it was saved for. */
class InMemoryDetailsRepositorySolver(
    typeCode: String,
) : DetailsRepositorySolver<AccountDetails> {
    private val accountTypeCode = AccountTypeCode(typeCode)
    val details = linkedMapOf<AccountId, AccountDetails>()

    fun seed(vararg details: AccountDetails) = apply { details.forEach { this.details[it.accountId] = it } }

    override fun supports(accountTypeCode: AccountTypeCode): Boolean = accountTypeCode == this.accountTypeCode

    override fun getAccountsDetailsByAccounts(accounts: List<Account>): List<AccountDetails> =
        accounts.filter { supports(it.accountTypeCode) }.mapNotNull { details[it.id] }

    override fun createAccountDetails(
        accountId: AccountId,
        accountDetails: AccountDetails,
    ) {
        details[accountId] = accountDetails
    }

    override fun updateAccountDetails(
        accountId: AccountId,
        accountDetails: AccountDetails,
    ) {
        check(accountId in details) { "No details to update for account ${accountId.value}" }
        details[accountId] = accountDetails
    }

    override fun deleteAccountDetails(accountId: AccountId) {
        details.remove(accountId)
    }
}
