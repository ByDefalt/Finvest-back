package com.example.finvest.account.domain

import java.math.BigDecimal
import java.time.LocalDateTime

data class Account(
    var id: Long,
    var bankId: Long,
    var name: String,
    var balance: BigDecimal,
    var currencyId: Long,
    var createdAt: LocalDateTime,
    var closedAt: LocalDateTime?,
    var accountStatusId: Long,
    var description: String?,
    val accountType: String
)