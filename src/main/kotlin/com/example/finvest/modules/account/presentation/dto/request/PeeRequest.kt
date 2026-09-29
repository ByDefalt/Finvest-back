package com.example.finvest.modules.account.presentation.dto.request

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

data class PeeRequest(
    val accountId: Long = 0L,
    val openingDate: LocalDate,
    val employer: String,
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
