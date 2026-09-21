package com.example.finvest.modules.auth.presentation.mapper

import com.example.finvest.modules.auth.domain.models.User
import com.example.finvest.modules.auth.presentation.dto.UserResponse

fun User.toDto(): UserResponse {
    return UserResponse(
        email = this.email
    )
}