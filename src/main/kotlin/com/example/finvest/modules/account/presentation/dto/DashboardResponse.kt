package com.example.finvest.modules.account.presentation.dto

import java.math.BigDecimal

data class DashboardResponse(
    val totalAccounts: Int,
    val totalBalance: BigDecimal,
    val totalActiveAccounts: Int,
    val accounts: List<AccountDashboardResponse>,
)
