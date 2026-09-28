package com.example.finvest.modules.auth.domain.repository

import com.example.finvest.modules.auth.domain.models.User
import com.example.finvest.modules.auth.domain.models.UserCredentials

interface AuthRepository {
    fun register(
        email: String,
        password: String,
    ): User

    fun findCredentialsByEmail(email: String): UserCredentials?

    fun findUserByEmail(email: String): User?

    fun findUserById(userId: Long): User?
}
