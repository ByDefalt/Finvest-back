package com.example.finvest.modules.account.presentation.dto

import java.math.BigDecimal
import java.time.LocalDateTime

data class AccountDto(
    val accountId: Long,
    val bankId: Long,
    val name: String,
    val balance: BigDecimal,
    val currencyId: Long,
    val createdAt: LocalDateTime,
    val closedAt: LocalDateTime?,
    val accountStatusId: Long,
    val description: String?,
    val accountTypeId: Long,
)
