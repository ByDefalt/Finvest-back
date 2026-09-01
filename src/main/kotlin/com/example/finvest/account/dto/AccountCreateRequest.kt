package com.example.finvest.account.dto

import jakarta.validation.constraints.NotBlank
import java.math.BigDecimal

data class AccountCreateRequest(
    @field:NotBlank
    val name: String,
    val balance: BigDecimal,
    @field:NotBlank
    val type: String,
)
