package com.example.finvest.modules.account.presentation.dto.request

import java.math.BigDecimal
import java.time.LocalDateTime

data class AccountRequest(
    val accountId: Long = 0L,
    val bankBic: String,
    val name: String,
    val balance: BigDecimal,
    val currency: String,
    val createdAt: LocalDateTime,
    val closedAt: LocalDateTime?,
    val accountStatus: AccountStatusDto,
    val description: String?,
    val accountType: String,
)
