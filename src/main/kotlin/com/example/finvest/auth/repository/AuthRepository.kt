package com.example.finvest.auth.repository

import com.example.finvest.auth.domain.User
import com.example.finvest.auth.domain.UserCredentials

interface AuthRepository {
    fun register(email: String, password: String): User

    fun findCredentialsByEmail(email: String): UserCredentials?

    fun findUserByEmail(email: String): User?
}