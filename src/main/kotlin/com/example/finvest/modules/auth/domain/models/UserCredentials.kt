package com.example.finvest.modules.auth.domain.models

data class UserCredentials(
    val id: Long,
    val email: String,
    val password: String
)