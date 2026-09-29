package com.example.finvest.modules.account.presentation.dto.request

import java.math.BigDecimal
import java.time.LocalDateTime

data class CompteCourantRequest(
    val accountId: Long = 0L,
    val iban: String,
    val bic: String,
    val accountNumber: String,
    val overdraftLimit: BigDecimal,
    val holderName: String,
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
