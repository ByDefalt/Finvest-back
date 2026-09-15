package com.example.finvest.auth.domain

data class Tokens(
    val accessToken: String,
    val refreshToken: String
)