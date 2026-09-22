package com.example.finvest.modules.auth.presentation.models

import jakarta.validation.constraints.NotNull

data class UserResponse(

    @field:NotNull
    val email: String
)