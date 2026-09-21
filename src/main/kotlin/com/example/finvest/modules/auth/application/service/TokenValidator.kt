package com.example.finvest.modules.auth.application.service

import com.example.finvest.modules.shared.application.security.AuthenticatedUser

interface TokenValidator {

    fun validateAccessToken(
        token: String
    ): AuthenticatedUser?

    fun validateRefreshToken(
        token: String
    ): Long?
}