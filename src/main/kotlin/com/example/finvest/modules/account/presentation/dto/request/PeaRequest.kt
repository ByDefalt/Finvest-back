package com.example.finvest.modules.account.presentation.dto.request

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

data class PeaRequest(
    val accountId: Long = 0L,
    val openingDate: LocalDate,
    val depositLimit: BigDecimal,
    val bankBic: String,
    val name: String,
    val balance: BigDecimal,
    val currencyCode: String,
    val createdAt: LocalDateTime,
    val closedAt: LocalDateTime?,
    val accountStatus: AccountStatusDto,
    val description: String?,
    val accountType: String,
)
