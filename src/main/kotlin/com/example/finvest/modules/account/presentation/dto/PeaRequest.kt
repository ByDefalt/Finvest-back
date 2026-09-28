package com.example.finvest.modules.account.presentation.dto

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

data class PeaRequest(
    val accountId: Long = 0L,
    val openingDate: LocalDate,
    val depositLimit: BigDecimal,
    val bankId: Long = 0L,
    val name: String = "",
    val balance: BigDecimal = BigDecimal.ZERO,
    val currencyId: Long = 0L,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val closedAt: LocalDateTime? = null,
    val accountStatusId: Long = 0L,
    val description: String? = null,
    val accountTypeId: Long = 0L,
)
