package com.example.finvest.modules.auth.application.service

interface TokenGenerator {

    fun generateAccessToken(
        userId: Long,
        email: String
    ): String

    fun generateRefreshToken(
        userId: Long
    ): String

}