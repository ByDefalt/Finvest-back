package com.example.finvest.modules.account.presentation.dto

import java.math.BigDecimal
import java.time.LocalDateTime

data class AccountDashboardResponse(
    val id: Long,
    val bankName: String,
    val name: String,
    val balance: BigDecimal,
    val currency: String,
    val createdAt: LocalDateTime,
    val closedAt: LocalDateTime?,
    val accountStatus: AccountStatusDto,
    val description: String?,
    val type: String,
    val accountOwners: List<AccountOwnerDashboardResponse>
)
