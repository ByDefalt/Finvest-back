package com.example.finvest.modules.auth.application.usecase

import com.example.finvest.logger.Logger
import com.example.finvest.modules.auth.application.service.PasswordHasher
import com.example.finvest.modules.auth.application.service.TokenGenerator
import com.example.finvest.modules.auth.domain.exception.PasswordMismatchException
import com.example.finvest.modules.auth.domain.exception.UserNotExistsException
import com.example.finvest.modules.auth.domain.models.Tokens
import com.example.finvest.modules.auth.domain.repository.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository,
    private val passwordHasher: PasswordHasher,
    private val tokenGenerator: TokenGenerator,
    private val logger: Logger,
) {
    operator fun invoke(
        email: String,
        password: String,
    ): Tokens {
        val existingUser = authRepository.findCredentialsByEmail(email)
        if (existingUser == null) {
            logger.warn("user not found email=$email")
            throw UserNotExistsException(email)
        }
        if (!passwordHasher.matches(
                password,
                existingUser.password,
            )
        ) {
            logger.warn("invalid password for email=$email")
            throw PasswordMismatchException(email)
        }
        val accessToken =
            tokenGenerator.generateAccessToken(
                existingUser.id,
                existingUser.email,
            )

        val refreshToken =
            tokenGenerator.generateRefreshToken(
                existingUser.id,
            )

        logger.info(
            "AuthService.login - success id=${existingUser.id} email=${existingUser.email}",
        )

        return Tokens(
            accessToken = accessToken,
            refreshToken = refreshToken,
        )
    }
}
