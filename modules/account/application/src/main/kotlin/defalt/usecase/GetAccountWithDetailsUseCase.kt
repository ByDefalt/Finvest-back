package defalt.usecase

import defalt.UserQueryApi
import defalt.exception.AccountErrorCode
import defalt.exception.CustomException
import defalt.logger.Logger
import defalt.manager.TransactionManager
import defalt.models.AccountOwnerWithIdentity
import defalt.models.AccountWithDetails
import defalt.models.AuthenticatedUser
import defalt.models.OwnerIdentity
import defalt.repository.AccountOwnerRepository
import defalt.repository.AccountRepository
import defalt.solver.DetailsRepositoryResolver
import defalt.valueobject.AccountId
import defalt.valueobject.OwnerRef.External
import defalt.valueobject.OwnerRef.Internal
import defalt.valueobject.OwnerRef.Unknown

/**
 * Lists the accounts of a user with their bank, details and owners.
 *
 * @param accountRepository persistence of the accounts.
 * @param accountOwnerRepository persistence of the account owners.
 * @param userQueryApi public API of the auth module, to read the users.
 * @param detailsRepositoryResolver persistence of the type-specific details of the accounts.
 * @param transactionManager runs the database work in transactions.
 * @param logger logger of the class.
 */
class GetAccountWithDetailsUseCase(
    private val accountRepository: AccountRepository,
    private val accountOwnerRepository: AccountOwnerRepository,
    private val userQueryApi: UserQueryApi,
    private val detailsRepositoryResolver: DetailsRepositoryResolver,
    private val transactionManager: TransactionManager,
    private val logger: Logger,
) {
    /**
     * Accounts owned by [authenticatedUser], read in a single read-only transaction.
     *
     * @param authenticatedUser user authenticated by the access token of the request.
     * @return the accounts of the user with their bank, details and owners; an empty list if there is none.
     * @throws CustomException `ACCOUNT_DETAILS_NOT_FOUND` or `OWNER_IDENTITY_NOT_FOUND` if the data are inconsistent.
     */
    operator fun invoke(authenticatedUser: AuthenticatedUser): List<AccountWithDetails<*>> =
        transactionManager
            .executeReadOnly {
                val accountsWithBanks = accountRepository.getAccountsWithBanksByUserId(authenticatedUser.userId)
                val accountIds = accountsWithBanks.map { it.account.id }.toSet()

                val detailsByAccount =
                    detailsRepositoryResolver
                        .getAccountsDetailsByAccounts(accountsWithBanks.map { it.account })
                        .associateBy { it.accountId }
                val ownersByAccount = loadOwnersWithIdentity(accountIds)

                accountsWithBanks.map { accountWithBank ->
                    val id = accountWithBank.account.id
                    AccountWithDetails(
                        accountWithBank,
                        detailsByAccount[id] ?: throw CustomException(
                            code = AccountErrorCode.ACCOUNT_DETAILS_NOT_FOUND,
                            message = "No details found for account ${id.value}",
                        ),
                        ownersByAccount[id].orEmpty(),
                    )
                }
            }.getOrThrow()

    /**
     * Owners of the accounts with their identity, grouped by account.
     *
     * @param accountIds ids of the accounts.
     * @return the owners of each account with their identity.
     * @throws CustomException `OWNER_IDENTITY_NOT_FOUND` if the identity of an owner cannot be found.
     */
    private fun loadOwnersWithIdentity(accountIds: Set<AccountId>): Map<AccountId, List<AccountOwnerWithIdentity>> {
        val owners = accountOwnerRepository.getAccountOwnersByAccountIds(accountIds)
        val refs = owners.map { it.owner }

        val internalSummaries =
            userQueryApi.findInternalSummaries(
                refs.filterIsInstance<Internal>().map { it.userId }.toSet(),
            )
        val externalSummaries =
            userQueryApi.findExternalSummaries(
                refs.filterIsInstance<External>().map { it.externalId }.toSet(),
            )

        return owners
            .map { owner ->
                val summary =
                    when (val ref = owner.owner) {
                        is Internal -> internalSummaries[ref.userId]
                        is External -> externalSummaries[ref.externalId]
                        is Unknown -> null
                    } ?: throw CustomException(
                        code = AccountErrorCode.OWNER_IDENTITY_NOT_FOUND,
                        message = "Identity not found for owner ${owner.id.value}",
                    )
                AccountOwnerWithIdentity(owner, OwnerIdentity(summary.firstName, summary.lastName, summary.email))
            }.groupBy { it.accountOwner.accountId }
    }
}
