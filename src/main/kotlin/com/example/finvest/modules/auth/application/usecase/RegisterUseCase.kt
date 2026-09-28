package com.example.finvest.modules.auth.application.usecase

import com.example.finvest.logger.Logger
import com.example.finvest.modules.auth.application.service.PasswordHasher
import com.example.finvest.modules.auth.domain.models.User
import com.example.finvest.modules.auth.domain.repository.AuthRepository

class RegisterUseCase(
    private val authRepository: AuthRepository,
    private val passwordHasher: PasswordHasher,
    private val logger: Logger,
) {
    operator fun invoke(
        email: String,
        password: String,
    ): User {
        val hashedPassword = passwordHasher.hash(password)

        val createdUser = authRepository.register(email, hashedPassword)

        return createdUser
    }
}
