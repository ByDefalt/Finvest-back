package defalt.config

import defalt.UserQueryApi
import defalt.logger.Logger
import defalt.manager.TransactionManager
import defalt.repository.AccountOwnerRepository
import defalt.repository.AccountRepository
import defalt.repository.AssuranceVieRepository
import defalt.repository.CompteCourantRepository
import defalt.repository.CompteTitresRepository
import defalt.repository.LivretRepository
import defalt.repository.PeaRepository
import defalt.repository.PeeRepository
import defalt.repository.PerRepository
import defalt.resolver.AccountOwnerResolver
import defalt.resolver.UserQueryAccountOwnerResolver
import defalt.solver.AssuranceVieDetailsRepositorySolver
import defalt.solver.CompteCourantDetailsRepositorySolver
import defalt.solver.CompteTitresDetailsRepositorySolver
import defalt.solver.DetailsRepositoryResolver
import defalt.solver.DetailsRepositorySolver
import defalt.solver.LivretDetailsRepositorySolver
import defalt.solver.PeaDetailsRepositorySolver
import defalt.solver.PeeDetailsRepositorySolver
import defalt.solver.PerDetailsRepositorySolver
import defalt.usecase.CreateAccountUseCase
import defalt.usecase.DeleteAccountUseCase
import defalt.usecase.GetAccountWithDetailsUseCase
import defalt.usecase.UpdateAccountUseCase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Composition root of the account module: builds its application services and use cases,
 * which do not depend on Spring, from the JDBC adapters of the module.
 */
@Configuration
class AccountApplicationConfig {
    /**
     * Details solver of the current accounts.
     *
     * @param repository persistence of the current accounts.
     * @return the solver.
     */
    @Bean
    fun compteCourantDetailsSolver(repository: CompteCourantRepository): DetailsRepositorySolver<*> =
        CompteCourantDetailsRepositorySolver(repository)

    /**
     * Details solver of the regulated savings accounts.
     *
     * @param repository persistence of the regulated savings accounts.
     * @return the solver.
     */
    @Bean
    fun livretDetailsSolver(repository: LivretRepository): DetailsRepositorySolver<*> = LivretDetailsRepositorySolver(repository)

    /**
     * Details solver of the PEAs.
     *
     * @param repository persistence of the PEAs.
     * @return the solver.
     */
    @Bean
    fun peaDetailsSolver(repository: PeaRepository): DetailsRepositorySolver<*> = PeaDetailsRepositorySolver(repository)

    /**
     * Details solver of the securities accounts.
     *
     * @param repository persistence of the securities accounts.
     * @return the solver.
     */
    @Bean
    fun compteTitresDetailsSolver(repository: CompteTitresRepository): DetailsRepositorySolver<*> =
        CompteTitresDetailsRepositorySolver(repository)

    /**
     * Details solver of the life insurance contracts.
     *
     * @param repository persistence of the life insurance contracts.
     * @return the solver.
     */
    @Bean
    fun assuranceVieDetailsSolver(repository: AssuranceVieRepository): DetailsRepositorySolver<*> =
        AssuranceVieDetailsRepositorySolver(repository)

    /**
     * Details solver of the PERs.
     *
     * @param repository persistence of the PERs.
     * @return the solver.
     */
    @Bean
    fun perDetailsSolver(repository: PerRepository): DetailsRepositorySolver<*> = PerDetailsRepositorySolver(repository)

    /**
     * Details solver of the PEEs.
     *
     * @param repository persistence of the PEEs.
     * @return the solver.
     */
    @Bean
    fun peeDetailsSolver(repository: PeeRepository): DetailsRepositorySolver<*> = PeeDetailsRepositorySolver(repository)

    /**
     * Resolver dispatching to the solver of each account type, built from every solver bean.
     *
     * @param solvers every details solver bean.
     * @return the resolver.
     */
    @Bean
    fun detailsResolver(solvers: List<DetailsRepositorySolver<*>>): DetailsRepositoryResolver = DetailsRepositoryResolver(solvers)

    /**
     * Resolves the owners of the accounts through the public API of the auth module.
     *
     * @param userQueryApi public API of the auth module, to read the users.
     * @return the resolver.
     */
    @Bean
    fun accountOwnerResolver(userQueryApi: UserQueryApi): AccountOwnerResolver = UserQueryAccountOwnerResolver(userQueryApi)

    /**
     * Use case listing the accounts of a user.
     *
     * @param accountRepository persistence of the accounts.
     * @param accountOwnerRepository persistence of the account owners.
     * @param userQueryApi public API of the auth module, to read the users.
     * @param detailsRepositoryResolver persistence of the type-specific details of the accounts.
     * @param transactionManager runs the database work in transactions.
     * @param logger logger of the class.
     * @return the use case.
     */
    @Bean
    fun getAccountWithDetails(
        accountRepository: AccountRepository,
        accountOwnerRepository: AccountOwnerRepository,
        userQueryApi: UserQueryApi,
        detailsRepositoryResolver: DetailsRepositoryResolver,
        transactionManager: TransactionManager,
        logger: Logger,
    ) = GetAccountWithDetailsUseCase(
        accountRepository = accountRepository,
        accountOwnerRepository = accountOwnerRepository,
        userQueryApi = userQueryApi,
        detailsRepositoryResolver = detailsRepositoryResolver,
        transactionManager = transactionManager,
        logger = logger,
    )

    /**
     * Use case creating an account.
     *
     * @param accountRepository persistence of the accounts.
     * @param accountOwnerRepository persistence of the account owners.
     * @param detailsRepositoryResolver persistence of the type-specific details of the accounts.
     * @param accountOwnerResolver resolves the emails of the owners to users.
     * @param transactionManager runs the database work in transactions.
     * @param logger logger of the class.
     * @return the use case.
     */
    @Bean
    fun createAccountUseCase(
        accountRepository: AccountRepository,
        accountOwnerRepository: AccountOwnerRepository,
        detailsRepositoryResolver: DetailsRepositoryResolver,
        accountOwnerResolver: AccountOwnerResolver,
        transactionManager: TransactionManager,
        logger: Logger,
    ) = CreateAccountUseCase(
        accountRepository = accountRepository,
        accountOwnerRepository = accountOwnerRepository,
        detailsRepositoryResolver = detailsRepositoryResolver,
        accountOwnerResolver = accountOwnerResolver,
        transactionManager = transactionManager,
        logger = logger,
    )

    /**
     * Use case updating an account.
     *
     * @param accountRepository persistence of the accounts.
     * @param accountOwnerRepository persistence of the account owners.
     * @param detailsRepositoryResolver persistence of the type-specific details of the accounts.
     * @param accountOwnerResolver resolves the emails of the owners to users.
     * @param transactionManager runs the database work in transactions.
     * @param logger logger of the class.
     * @return the use case.
     */
    @Bean
    fun updateAccountUseCase(
        accountRepository: AccountRepository,
        accountOwnerRepository: AccountOwnerRepository,
        detailsRepositoryResolver: DetailsRepositoryResolver,
        accountOwnerResolver: AccountOwnerResolver,
        transactionManager: TransactionManager,
        logger: Logger,
    ) = UpdateAccountUseCase(
        accountRepository = accountRepository,
        accountOwnerRepository = accountOwnerRepository,
        detailsRepositoryResolver = detailsRepositoryResolver,
        accountOwnerResolver = accountOwnerResolver,
        transactionManager = transactionManager,
        logger = logger,
    )

    /**
     * Use case deleting an account.
     *
     * @param accountRepository persistence of the accounts.
     * @param accountOwnerRepository persistence of the account owners.
     * @param detailsRepositoryResolver persistence of the type-specific details of the accounts.
     * @param transactionManager runs the database work in transactions.
     * @param logger logger of the class.
     * @return the use case.
     */
    @Bean
    fun deleteAccountUseCase(
        accountRepository: AccountRepository,
        accountOwnerRepository: AccountOwnerRepository,
        detailsRepositoryResolver: DetailsRepositoryResolver,
        transactionManager: TransactionManager,
        logger: Logger,
    ) = DeleteAccountUseCase(
        accountRepository = accountRepository,
        accountOwnerRepository = accountOwnerRepository,
        detailsRepositoryResolver = detailsRepositoryResolver,
        transactionManager = transactionManager,
        logger = logger,
    )
}
