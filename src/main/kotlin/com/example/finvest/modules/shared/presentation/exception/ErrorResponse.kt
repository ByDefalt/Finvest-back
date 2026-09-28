package com.example.finvest.modules.shared.presentation.exception

data class ErrorResponse(
    val status: Int,
    val message: String,
    val errors: Map<String, String?> = emptyMap(),
)
