package com.example.finvest.modules.auth.presentation.mapper

import com.example.finvest.modules.auth.domain.models.Token
import com.example.finvest.modules.auth.domain.models.Tokens
import com.example.finvest.modules.auth.domain.models.User
import com.example.finvest.modules.auth.presentation.models.LoginResponse
import com.example.finvest.modules.auth.presentation.models.UserResponse

fun User.toDto(): UserResponse =
    UserResponse(
        email = this.email,
    )

fun Token.toLoginResponse(): LoginResponse =
    LoginResponse(
        token = this.value,
    )

fun Tokens.toLoginResponse(): LoginResponse =
    LoginResponse(
        token = this.accessToken,
    )

fun Tokens.toRefreshToken(): String = this.refreshToken
