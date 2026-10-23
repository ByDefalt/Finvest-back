package defalt.solver

import defalt.exception.AccountErrorCode
import defalt.exception.CustomException
import defalt.models.Account
import defalt.models.AccountDetails
import defalt.valueobject.AccountId
import defalt.valueobject.AccountTypeCode

/**
 * Dispatches the persistence of the account details to the [DetailsRepositorySolver] of the account type.
 *
 * @param solvers one solver per supported account type.
 */
class DetailsRepositoryResolver(
    private val solvers: List<DetailsRepositorySolver<*>>,
) {
    /**
     * Details of [accounts], whatever their type.
     *
     * @param accounts accounts of any type.
     * @return the details of these accounts, in no particular order.
     */
    fun getAccountsDetailsByAccounts(accounts: List<Account>): List<AccountDetails> =
        solvers.flatMap { it.getAccountsDetailsByAccounts(accounts) }

    /**
     * Persists the details of a new account.
     *
     * @param accountId id of the new account.
     * @param accountTypeCode type of the account (`PEA`, `LIVRET`...).
     * @param accountDetails details to persist.
     * @throws CustomException `INVALID_ACCOUNT_TYPE` if no solver supports [accountTypeCode].
     */
    fun createAccountDetails(
        accountId: AccountId,
        accountTypeCode: AccountTypeCode,
        accountDetails: AccountDetails,
    ) = solverFor(accountTypeCode).createAccountDetails(accountId, accountDetails)

    /**
     * Updates the details of an account.
     *
     * @param accountId id of the account.
     * @param accountTypeCode type of the account (`PEA`, `LIVRET`...).
     * @param accountDetails new details of the account.
     * @throws CustomException `INVALID_ACCOUNT_TYPE` if no solver supports [accountTypeCode].
     */
    fun updateAccountDetails(
        accountId: AccountId,
        accountTypeCode: AccountTypeCode,
        accountDetails: AccountDetails,
    ) = solverFor(accountTypeCode).updateAccountDetails(accountId, accountDetails)

    /**
     * Deletes the details of an account.
     *
     * @param accountId id of the account.
     * @param accountTypeCode type of the account (`PEA`, `LIVRET`...).
     * @throws CustomException `INVALID_ACCOUNT_TYPE` if no solver supports [accountTypeCode].
     */
    fun deleteAccountDetails(
        accountId: AccountId,
        accountTypeCode: AccountTypeCode,
    ) = solverFor(accountTypeCode).deleteAccountDetails(accountId)

    /**
     * Solver of [accountTypeCode].
     *
     * @param accountTypeCode type of the account (`PEA`, `LIVRET`...).
     * @return the solver supporting [accountTypeCode].
     * @throws CustomException `INVALID_ACCOUNT_TYPE` if no solver supports the type.
     */
    @Suppress("UNCHECKED_CAST")
    private fun solverFor(accountTypeCode: AccountTypeCode): DetailsRepositorySolver<AccountDetails> =
        solvers.find { it.supports(accountTypeCode) } as DetailsRepositorySolver<AccountDetails>?
            ?: throw CustomException(
                code = AccountErrorCode.INVALID_ACCOUNT_TYPE,
                message = "No details repository found for account type code: ${accountTypeCode.value}",
            )
}
