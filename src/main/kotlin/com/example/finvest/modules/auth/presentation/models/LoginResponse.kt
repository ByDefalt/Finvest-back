package com.example.finvest.modules.auth.presentation.models

import jakarta.validation.constraints.NotNull

data class LoginResponse(
    @field:NotNull
    val token: String,
)
