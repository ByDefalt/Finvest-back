package defalt.usecase

import defalt.exception.AccountErrorCode
import defalt.exception.CustomException
import defalt.logger.Logger
import defalt.manager.TransactionManager
import defalt.models.AuthenticatedUser
import defalt.policy.AccountAccessPolicy
import defalt.repository.AccountOwnerRepository
import defalt.repository.AccountRepository
import defalt.solver.DetailsRepositoryResolver
import defalt.valueobject.AccountId

/**
 * Deletes an account with its details and its owners.
 *
 * @param accountRepository persistence of the accounts.
 * @param accountOwnerRepository persistence of the account owners.
 * @param detailsRepositoryResolver persistence of the type-specific details of the accounts.
 * @param transactionManager runs the database work in transactions.
 * @param logger logger of the class.
 */
class DeleteAccountUseCase(
    private val accountRepository: AccountRepository,
    private val accountOwnerRepository: AccountOwnerRepository,
    private val detailsRepositoryResolver: DetailsRepositoryResolver,
    private val transactionManager: TransactionManager,
    private val logger: Logger,
) {
    /**
     * Deletes the owners, the details and the account, in a single transaction.
     *
     * @param authenticatedUser user deleting the account, who must be one of its owners.
     * @param accountId account to delete.
     * @throws CustomException `ACCOUNT_NOT_FOUND`, `ACCOUNT_ACCESS_DENIED`,
     * or `INTEGRITY_VIOLATION` if other data still reference the account.
     */
    operator fun invoke(
        authenticatedUser: AuthenticatedUser,
        accountId: AccountId,
    ) {
        logger.info(
            "DeleteAccountUseCase started: accountId=${accountId.value}, " +
                "authenticatedUserId=${authenticatedUser.userId.value}",
        )

        transactionManager
            .execute {
                val account =
                    accountRepository.getAccountByAccountId(accountId)
                        ?: throw CustomException(
                            code = AccountErrorCode.ACCOUNT_NOT_FOUND,
                            message = "Account ${accountId.value} not found",
                        )
                AccountAccessPolicy.checkIsOwner(
                    accountId,
                    accountOwnerRepository.getAccountOwnersByAccountIds(setOf(accountId)),
                    authenticatedUser.userId,
                )

                accountOwnerRepository.deleteAccountOwnersByAccountId(accountId)
                detailsRepositoryResolver.deleteAccountDetails(accountId, account.accountTypeCode)
                accountRepository.deleteAccount(accountId)
            }.onFailure {
                logger.error("DeleteAccountUseCase failed: ${it.message}", it)
            }.getOrThrow()

        logger.info("DeleteAccountUseCase completed: accountId=${accountId.value}")
    }
}
