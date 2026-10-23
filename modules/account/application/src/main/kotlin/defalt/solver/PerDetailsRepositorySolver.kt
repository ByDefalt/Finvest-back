package defalt.solver

import defalt.models.Account
import defalt.models.Per
import defalt.repository.PerRepository
import defalt.valueobject.AccountId
import defalt.valueobject.AccountTypeCode

/**
 * [DetailsRepositorySolver] of the PER (plan d'épargne retraite, retirement savings plan) (`PER`) accounts.
 *
 * @param repository persistence of the PER details.
 */
class PerDetailsRepositorySolver(
    private val repository: PerRepository,
) : DetailsRepositorySolver<Per> {
    /**
     * Whether this solver handles the details of [accountTypeCode].
     *
     * @param accountTypeCode type of the account (`PEA`, `LIVRET`...).
     * @return true if this solver handles the type.
     */
    override fun supports(accountTypeCode: AccountTypeCode): Boolean = accountTypeCode.value == "PER"

    /**
     * Details of the accounts of the supported type among [accounts]; the other accounts are ignored.
     *
     * @param accounts accounts of any type.
     * @return the details of the accounts of the supported type.
     */
    override fun getAccountsDetailsByAccounts(accounts: List<Account>): List<Per> {
        val ids =
            accounts
                .filter { supports(it.accountTypeCode) }
                .map { it.id }
                .toSet()
        return repository.getPersByAccountIds(ids)
    }

    /**
     * Persists the details of the new account [accountId], whatever the account id they carry.
     *
     * @param accountId id of the new account.
     * @param accountDetails details to persist.
     */
    override fun createAccountDetails(
        accountId: AccountId,
        accountDetails: Per,
    ) {
        repository.createPer(accountDetails.copy(accountId = accountId))
    }

    /**
     * Updates the details of [accountId], whatever the account id they carry.
     *
     * @param accountId id of the account.
     * @param accountDetails new details of the account.
     */
    override fun updateAccountDetails(
        accountId: AccountId,
        accountDetails: Per,
    ) {
        repository.updatePer(accountDetails.copy(accountId = accountId))
    }

    /**
     * Deletes the details of [accountId].
     *
     * @param accountId id of the account.
     */
    override fun deleteAccountDetails(accountId: AccountId) {
        repository.deletePer(accountId)
    }
}
