package com.example.finvest.auth.jwt

import com.example.finvest.common.dto.AuthenticatedUser

interface JwtService {

    fun generateAccessToken(
        userId: Long,
        email: String
    ): String

    fun generateRefreshToken(
        userId: Long
    ): String

    fun validateAccessToken(
        token: String
    ): AuthenticatedUser?

    fun validateRefreshToken(
        token: String
    ): Long?
}