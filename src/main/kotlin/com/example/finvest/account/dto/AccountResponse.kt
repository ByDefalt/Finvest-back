package com.example.finvest.account.dto

import jakarta.validation.constraints.Positive
import java.math.BigDecimal
import java.time.LocalDateTime

data class AccountResponse(
    @field:Positive
    val id: Long,
    val bankId: Long,
    val name: String,
    val balance: BigDecimal,
    val currencyId: Long,
    val createdAt: LocalDateTime,
    val closedAt: LocalDateTime?,
    val accountStatusId: Long,
    val description: String?
)