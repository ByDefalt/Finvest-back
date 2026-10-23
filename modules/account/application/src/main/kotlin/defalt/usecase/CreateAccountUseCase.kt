package defalt.usecase

import defalt.command.CreateAccountCommand
import defalt.logger.Logger
import defalt.manager.TransactionManager
import defalt.models.AuthenticatedUser
import defalt.policy.AccountOwnershipPolicy
import defalt.repository.AccountOwnerRepository
import defalt.repository.AccountRepository
import defalt.resolver.AccountOwnerResolver
import defalt.resolver.ownerOf
import defalt.solver.DetailsRepositoryResolver

/**
 * Creates an account with its details and its owners.
 *
 * @param accountRepository persistence of the accounts.
 * @param accountOwnerRepository persistence of the account owners.
 * @param detailsRepositoryResolver persistence of the type-specific details of the accounts.
 * @param accountOwnerResolver resolves the emails of the owners to users.
 * @param transactionManager runs the database work in transactions.
 * @param logger logger of the class.
 */
class CreateAccountUseCase(
    private val accountRepository: AccountRepository,
    private val accountOwnerRepository: AccountOwnerRepository,
    private val detailsRepositoryResolver: DetailsRepositoryResolver,
    private val accountOwnerResolver: AccountOwnerResolver,
    private val transactionManager: TransactionManager,
    private val logger: Logger,
) {
    /**
     * Checks the ownership rules, resolves the owners, then creates the account, its details and its owners
     * in a single transaction: nothing is written if one of them fails.
     *
     * @param authenticatedUser creator of the account, who must be one of its owners.
     * @param command the account to create.
     * @throws CustomException the codes of `AccountOwnershipPolicy`, `OWNER_NOT_FOUND`, `INVALID_ACCOUNT_TYPE`,
     * or `DUPLICATE` if a unique value of the details already exists.
     */
    operator fun invoke(
        authenticatedUser: AuthenticatedUser,
        command: CreateAccountCommand,
    ) {
        val (account, details, owners) = command
        logger.info(
            "CreateAccountUseCase started: accountType=${account.accountTypeCode.value}, " +
                "ownerCount=${owners.size}, authenticatedUserId=${authenticatedUser.userId.value}",
        )
        AccountOwnershipPolicy.check(owners, authenticatedUser.email)

        val accountId =
            transactionManager
                .execute {
                    val ownerRefs = accountOwnerResolver.resolve(owners.map { it.email }.toSet())

                    val accountId = accountRepository.createAccount(account)
                    detailsRepositoryResolver.createAccountDetails(accountId, account.accountTypeCode, details)
                    accountOwnerRepository.createAccountOwners(
                        owners.map { it.toAccountOwner(accountId, ownerRefs.ownerOf(it.email)) },
                    )
                    accountId
                }.onFailure {
                    logger.error("CreateAccountUseCase failed: ${it.message}", it)
                }.getOrThrow()

        logger.info("CreateAccountUseCase completed: accountId=${accountId.value}, ownerCount=${owners.size}")
    }
}
