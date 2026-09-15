package com.example.finvest.account.dto

import java.math.BigDecimal

data class DashboardResponse(
    val totalAccounts: Int,
    val totalBalance: BigDecimal,
    val totalActiveAccounts: Int,
    val accounts: List<AccountDashboardResponse>,
)
