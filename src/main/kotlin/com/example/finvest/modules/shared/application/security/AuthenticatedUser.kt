package com.example.finvest.modules.shared.application.security

data class AuthenticatedUser(
    val id: Long,
    val email: String,
)