package defalt.config

import defalt.logger.Logger
import defalt.manager.TransactionManager
import defalt.repository.PersonalInformationRepository
import defalt.repository.UserRepository
import defalt.service.PasswordHasher
import defalt.service.TokenGenerator
import defalt.service.TokenValidator
import defalt.usecase.LoginUseCase
import defalt.usecase.RefreshAccessTokenUseCase
import defalt.usecase.RegisterUseCase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AuthApplicationConfig {
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
}
