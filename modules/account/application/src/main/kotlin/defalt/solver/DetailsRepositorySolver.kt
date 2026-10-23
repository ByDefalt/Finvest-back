package defalt.solver

import defalt.models.Account
import defalt.models.AccountDetails
import defalt.valueobject.AccountId
import defalt.valueobject.AccountTypeCode

/**
 * Persistence of the details of one account type. Each type has its own solver, and
 * [DetailsRepositoryResolver] picks the solver of the account: adding a type means adding a solver.
 *
 * @param T type of the details.
 */
interface DetailsRepositorySolver<T : AccountDetails> {
    /**
     * Whether this solver handles the details of [accountTypeCode].
     *
     * @param accountTypeCode type of the account (`PEA`, `LIVRET`...).
     * @return true if this solver handles the type.
     */
    fun supports(accountTypeCode: AccountTypeCode): Boolean

    /**
     * Details of the accounts of the supported type among [accounts]; the other accounts are ignored.
     *
     * @param accounts accounts of any type.
     * @return the details of the accounts of the supported type.
     */
    fun getAccountsDetailsByAccounts(accounts: List<Account>): List<T>

    /**
     * Persists the details of the new account [accountId], whatever the account id they carry.
     *
     * @param accountId id of the new account.
     * @param accountDetails details to persist.
     */
    fun createAccountDetails(
        accountId: AccountId,
        accountDetails: T,
    )

    /**
     * Updates the details of [accountId], whatever the account id they carry.
     *
     * @param accountId id of the account.
     * @param accountDetails new details of the account.
     */
    fun updateAccountDetails(
        accountId: AccountId,
        accountDetails: T,
    )

    /**
     * Deletes the details of [accountId].
     *
     * @param accountId id of the account.
     */
    fun deleteAccountDetails(accountId: AccountId)
}
