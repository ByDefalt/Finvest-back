package defalt.config

import defalt.logger.Logger
import defalt.manager.TransactionManager
import defalt.repository.ExternalUserRepository
import defalt.repository.PersonalInformationRepository
import defalt.repository.UserRepository
import defalt.service.PasswordHasher
import defalt.service.TokenGenerator
import defalt.service.TokenValidator
import defalt.usecase.LoginUseCase
import defalt.usecase.RefreshAccessTokenUseCase
import defalt.usecase.RegisterExternalUseCase
import defalt.usecase.RegisterUseCase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/** Composition root of the auth module: builds its use cases, which do not depend on Spring. */
@Configuration
class AuthApplicationConfig {
    /**
     * Use case registering an internal user.
     *
     * @param passwordHasher hashes and checks the passwords.
     * @param userRepository persistence of the internal users.
     * @param personalInformationRepository persistence of the identities of the users.
     * @param transactionManager runs the database work in transactions.
     * @param logger logger of the class.
     * @return the use case.
     */
    @Bean
    fun registerUseCase(
        passwordHasher: PasswordHasher,
        userRepository: UserRepository,
        personalInformationRepository: PersonalInformationRepository,
        transactionManager: TransactionManager,
        logger: Logger,
    ) = RegisterUseCase(
        userRepository = userRepository,
        passwordHasher = passwordHasher,
        logger = logger,
        personalInformationRepository = personalInformationRepository,
        transactionManager = transactionManager,
    )

    /**
     * Use case logging an internal user in.
     *
     * @param passwordHasher hashes and checks the passwords.
     * @param userRepository persistence of the internal users.
     * @param tokenGenerator generates the authentication tokens.
     * @param logger logger of the class.
     * @return the use case.
     */
    @Bean
    fun loginUseCase(
        passwordHasher: PasswordHasher,
        userRepository: UserRepository,
        tokenGenerator: TokenGenerator,
        logger: Logger,
    ) = LoginUseCase(
        userRepository = userRepository,
        passwordHasher = passwordHasher,
        tokenGenerator = tokenGenerator,
        logger = logger,
    )

    /**
     * Use case exchanging a refresh token for an access token.
     *
     * @param userRepository persistence of the internal users.
     * @param personalInformationRepository persistence of the identities of the users.
     * @param tokenGenerator generates the authentication tokens.
     * @param tokenValidator validates the authentication tokens.
     * @param logger logger of the class.
     * @return the use case.
     */
    @Bean
    fun refreshAccessTokenUseCase(
        userRepository: UserRepository,
        personalInformationRepository: PersonalInformationRepository,
        tokenGenerator: TokenGenerator,
        tokenValidator: TokenValidator,
        logger: Logger,
    ) = RefreshAccessTokenUseCase(
        tokenValidator = tokenValidator,
        tokenGenerator = tokenGenerator,
        userRepository = userRepository,
        logger = logger,
        personalInformationRepository = personalInformationRepository,
    )

    /**
     * Use case registering an external user.
     *
     * @param externalUserRepository persistence of the external users.
     * @param personalInformationRepository persistence of the identities of the users.
     * @param transactionManager runs the database work in transactions.
     * @param logger logger of the class.
     * @return the use case.
     */
    @Bean
    fun registerExternalUseCase(
        externalUserRepository: ExternalUserRepository,
        personalInformationRepository: PersonalInformationRepository,
        transactionManager: TransactionManager,
        logger: Logger,
    ) = RegisterExternalUseCase(
        externalUserRepository = externalUserRepository,
        personalInformationRepository = personalInformationRepository,
        transactionManager = transactionManager,
        logger = logger,
    )
}
