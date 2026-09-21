package com.example.finvest.modules.common.exeption

data class ErrorResponse(
    val status: Int,
    val message: String,
    val errors: Map<String, String?> = emptyMap(),
)