package com.example.finvest.modules.account.presentation.dto.response

import com.example.finvest.modules.account.presentation.dto.request.AccountStatusDto
import java.math.BigDecimal
import java.time.LocalDateTime

data class AccountDto(
    val accountId: Long,
    val bank: BankDto,
    val name: String,
    val balance: BigDecimal,
    val currency: String,
    val createdAt: LocalDateTime,
    val closedAt: LocalDateTime?,
    val accountStatus: AccountStatusDto,
    val description: String?,
    val accountType: String,
)
