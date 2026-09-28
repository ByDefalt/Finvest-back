package com.example.finvest.modules.account.domain.models

import com.example.finvest.modules.account.domain.valueobject.AccountId
import com.example.finvest.modules.account.domain.valueobject.AccountStatusId
import com.example.finvest.modules.account.domain.valueobject.AccountTypeId
import com.example.finvest.modules.account.domain.valueobject.BankId
import java.time.LocalDateTime

data class Account(
    val id: AccountId,
    val bankId: BankId,
    val name: String,
    val balance: Money,
    val createdAt: LocalDateTime,
    val closedAt: LocalDateTime?,
    val accountStatusId: AccountStatusId,
    val description: String?,
    val accountTypeId: AccountTypeId,
)
