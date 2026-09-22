package com.example.finvest.modules.account.presentation.dto

import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class DashboardResponse(
    @field:NotNull
    val totalAccounts: Int,
    @field:NotNull
    val totalBalance: BigDecimal,
    @field:NotNull
    val totalActiveAccounts: Int,
    @field:NotNull
    val accounts: List<AccountDashboardResponse>,
)
