package com.example.finvest.modules.auth.application.usecase

import com.example.finvest.logger.Logger
import com.example.finvest.modules.auth.application.service.TokenGenerator
import com.example.finvest.modules.auth.application.service.TokenValidator
import com.example.finvest.modules.auth.domain.exception.UserNotExistsException
import com.example.finvest.modules.auth.domain.models.Token
import com.example.finvest.modules.auth.domain.repository.AuthRepository

class RefreshAccessTokenUseCase(
    private val tokenValidator: TokenValidator,
    private val tokenGenerator: TokenGenerator,
    private val authRepository: AuthRepository,
    private val logger: Logger
) {
    operator fun invoke(refreshToken: String): Token {
        val userId = tokenValidator.validateRefreshToken(refreshToken)

        if (userId == null) {
            logger.warn("invalid refresh token")
            throw IllegalArgumentException("Invalid refresh token")
        }

        val user = authRepository.findUserById(userId)
            ?: throw UserNotExistsException(userId.toString())

        logger.info("success id=${user.id} email=${user.email}")

        return Token(
            tokenGenerator.generateAccessToken(
                user.id,
                user.email
            )
        )
    }
}