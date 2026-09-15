package com.example.finvest.account.dto

import jakarta.validation.constraints.NotBlank
import java.math.BigDecimal
import java.time.LocalDateTime

data class AccountUpdateRequest(
    val id: Long,

    @field:NotBlank
    val name: String,

    val balance: BigDecimal,
    val bankId: Long,
    val currencyId: Long,
    val accountStatusId: Long,
    val closedAt: LocalDateTime?,
    val description: String?
)