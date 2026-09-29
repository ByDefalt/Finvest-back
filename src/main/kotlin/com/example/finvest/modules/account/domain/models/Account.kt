package com.example.finvest.modules.account.domain.models

import com.example.finvest.modules.account.domain.valueobject.AccountId
import com.example.finvest.modules.account.domain.valueobject.AccountType
import java.time.LocalDateTime

data class Account(
    val id: AccountId,
    val bank: Bank,
    val name: String,
    val balance: Money,
    val createdAt: LocalDateTime,
    val closedAt: LocalDateTime?,
    val accountStatus: AccountStatus,
    val description: String?,
    val accountType: AccountType,
)
