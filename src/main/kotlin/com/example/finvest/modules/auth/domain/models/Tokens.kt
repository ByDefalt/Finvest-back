package com.example.finvest.modules.auth.domain.models

data class Tokens(
    val accessToken: String,
    val refreshToken: String,
)
