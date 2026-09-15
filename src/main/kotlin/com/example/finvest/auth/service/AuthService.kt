package com.example.finvest.auth.service

import com.example.finvest.auth.domain.Token
import com.example.finvest.auth.domain.Tokens
import com.example.finvest.auth.domain.User
import com.example.finvest.auth.exeption.PasswordMismatchException
import com.example.finvest.auth.exeption.UserAlreadyExistsException
import com.example.finvest.auth.exeption.UserNotExistsException
import com.example.finvest.auth.jwt.JwtService
import com.example.finvest.auth.repository.AuthRepository
import com.example.finvest.common.config.SecurityConfig
import com.example.finvest.common.logger.Logger
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: AuthRepository,
    private val jwtService: JwtService,
    private val securityConfig: SecurityConfig,
    private val logger: Logger
) {

    fun register(email: String, password: String): User {
        logger.debug("AuthService.createUser - start for email=$email")

        try {
            val hashedPassword =
                securityConfig.passwordEncoder().encode(password) as String

            val createdUser =
                userRepository.register(email, hashedPassword)

            logger.debug(
                "AuthService.createUser - repository create ok email=$email"
            )

            return createdUser

        } catch (e: DataIntegrityViolationException) {
            logger.warn(
                "AuthService.createUser - user already exists email=$email"
            )

            throw UserAlreadyExistsException(email)
        }
    }

    fun login(email: String, password: String): Tokens {

        val existingUser = userRepository.findCredentialsByEmail(email)

        if (existingUser == null) {
            logger.warn(
                "AuthService.login - user not found email=$email"
            )

            throw UserNotExistsException(email)
        }

        if (!securityConfig.passwordEncoder().matches(
                password,
                existingUser.password
            )
        ) {
            logger.warn(
                "AuthService.login - invalid password for email=$email"
            )

            throw PasswordMismatchException(email)
        }

        val accessToken = jwtService.generateAccessToken(
            existingUser.id,
            existingUser.email
        )

        val refreshToken = jwtService.generateRefreshToken(
            existingUser.id
        )

        logger.info(
            "AuthService.login - success id=${existingUser.id} email=${existingUser.email}"
        )

        return Tokens(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }

    fun refreshToken(refreshToken: String): Token {

        val userId = jwtService.validateRefreshToken(refreshToken)

        if (userId == null) {
            logger.warn(
                "AuthService.refreshToken - invalid refresh token"
            )

            throw IllegalArgumentException("Invalid refresh token")
        }

        val user = userRepository.findUserById(userId)
            ?: throw UserNotExistsException(userId.toString())

        logger.info(
            "AuthService.refreshToken - success id=${user.id} email=${user.email}"
        )

        return Token(
            jwtService.generateAccessToken(
                user.id,
                user.email
            )
        )
    }
}