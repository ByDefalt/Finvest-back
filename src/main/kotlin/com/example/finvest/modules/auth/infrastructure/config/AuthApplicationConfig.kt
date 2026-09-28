package com.example.finvest.modules.auth.infrastructure.config

import com.example.finvest.logger.Logger
import com.example.finvest.modules.auth.application.service.PasswordHasher
import com.example.finvest.modules.auth.application.service.TokenGenerator
import com.example.finvest.modules.auth.application.service.TokenValidator
import com.example.finvest.modules.auth.application.usecase.LoginUseCase
import com.example.finvest.modules.auth.application.usecase.RefreshAccessTokenUseCase
import com.example.finvest.modules.auth.application.usecase.RegisterUseCase
import com.example.finvest.modules.auth.domain.repository.AuthRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AuthApplicationConfig {
    @Bean
    fun registerUseCase(
        passwordHasher: PasswordHasher,
        authRepository: AuthRepository,
        logger: Logger,
    ) = RegisterUseCase(
        authRepository = authRepository,
        passwordHasher = passwordHasher,
        logger = logger,
    )

    @Bean
    fun loginUseCase(
        passwordHasher: PasswordHasher,
        authRepository: AuthRepository,
        tokenGenerator: TokenGenerator,
        logger: Logger,
    ) = LoginUseCase(
        authRepository = authRepository,
        passwordHasher = passwordHasher,
        tokenGenerator = tokenGenerator,
        logger = logger,
    )

    @Bean
    fun refreshAccessTokenUseCase(
        authRepository: AuthRepository,
        tokenGenerator: TokenGenerator,
        tokenValidator: TokenValidator,
        logger: Logger,
    ) = RefreshAccessTokenUseCase(
        tokenValidator = tokenValidator,
        tokenGenerator = tokenGenerator,
        authRepository = authRepository,
        logger = logger,
    )
}
