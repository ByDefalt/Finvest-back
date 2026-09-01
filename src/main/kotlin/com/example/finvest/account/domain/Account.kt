package com.example.finvest.account.domain

import java.math.BigDecimal

data class Account(
    val id: Long,
    val name: String,
    val balance: BigDecimal,
    val type: AccountType,
    val userId: Long
)