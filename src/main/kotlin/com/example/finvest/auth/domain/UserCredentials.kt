package com.example.finvest.auth.domain

data class UserCredentials(
    val id: Long,
    val email: String,
    val password: String
)