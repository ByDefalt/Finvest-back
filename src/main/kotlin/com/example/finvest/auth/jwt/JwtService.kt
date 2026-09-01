package com.example.finvest.auth.jwt

import com.example.finvest.common.dto.AuthenticatedUser

interface JwtService {

    fun generate(userId: Long, email: String): String

    fun validate(token: String): AuthenticatedUser?
}