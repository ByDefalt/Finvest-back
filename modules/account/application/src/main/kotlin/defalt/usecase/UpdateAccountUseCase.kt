package defalt.usecase

import defalt.command.UpdateAccountCommand
import defalt.exception.AccountErrorCode
import defalt.exception.CustomException
import defalt.logger.Logger
import defalt.manager.TransactionManager
import defalt.models.AuthenticatedUser
import defalt.policy.AccountAccessPolicy
import defalt.policy.AccountOwnershipPolicy
import defalt.policy.AccountUpdatePolicy
import defalt.repository.AccountOwnerRepository
import defalt.repository.AccountRepository
import defalt.resolver.AccountOwnerResolver
import defalt.resolver.ownerOf
import defalt.solver.DetailsRepositoryResolver

/**
 * Updates an account, its details and its owners.
 *
 * @param accountRepository persistence of the accounts.
 * @param accountOwnerRepository persistence of the account owners.
 * @param detailsRepositoryResolver persistence of the type-specific details of the accounts.
 * @param accountOwnerResolver resolves the emails of the owners to users.
 * @param transactionManager runs the database work in transactions.
 * @param logger logger of the class.
 */
class UpdateAccountUseCase(
    private val accountRepository: AccountRepository,
    private val accountOwnerRepository: AccountOwnerRepository,
    private val detailsRepositoryResolver: DetailsRepositoryResolver,
    private val accountOwnerResolver: AccountOwnerResolver,
    private val transactionManager: TransactionManager,
    private val logger: Logger,
) {
    /**
     * Checks the ownership rules, the access to the account and that its type does not change, then updates
     * the account and its details and replaces its owners, in a single transaction.
     *
     * @param authenticatedUser user updating the account, who must be one of its current and new owners.
     * @param command the account to update.
     * @throws CustomException the codes of `AccountOwnershipPolicy`, `ACCOUNT_NOT_FOUND`, `ACCOUNT_ACCESS_DENIED`,
     * `ACCOUNT_TYPE_IMMUTABLE` or `OWNER_NOT_FOUND`.
     */
    operator fun invoke(
        authenticatedUser: AuthenticatedUser,
        command: UpdateAccountCommand,
    ) {
        val (account, details, owners) = command
        logger.info(
            "UpdateAccountUseCase started: accountId=${account.id.value}, " +
                "ownerCount=${owners.size}, authenticatedUserId=${authenticatedUser.userId.value}",
        )
        AccountOwnershipPolicy.check(owners, authenticatedUser.email)

        transactionManager
            .execute {
                val existing =
                    accountRepository.getAccountByAccountId(account.id)
                        ?: throw CustomException(
                            code = AccountErrorCode.ACCOUNT_NOT_FOUND,
                            message = "Account ${account.id.value} not found",
                        )
                AccountAccessPolicy.checkIsOwner(
                    account.id,
                    accountOwnerRepository.getAccountOwnersByAccountIds(setOf(account.id)),
                    authenticatedUser.userId,
                )
                AccountUpdatePolicy.check(existing, account)

                val ownerRefs = accountOwnerResolver.resolve(owners.map { it.email }.toSet())

                accountRepository.updateAccount(account)
                detailsRepositoryResolver.updateAccountDetails(account.id, account.accountTypeCode, details)
                accountOwnerRepository.deleteAccountOwnersByAccountId(account.id)
                accountOwnerRepository.createAccountOwners(
                    owners.map { it.toAccountOwner(account.id, ownerRefs.ownerOf(it.email)) },
                )
            }.onFailure {
                logger.error("UpdateAccountUseCase failed: ${it.message}", it)
            }.getOrThrow()

        logger.info("UpdateAccountUseCase completed: accountId=${account.id.value}")
    }
}
