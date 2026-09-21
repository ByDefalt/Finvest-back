package com.example.finvest.modules.account.presentation.dto

import java.math.BigDecimal

data class AccountOwnerDashboardResponse(
    val email: String?,
    val name: String?,
    val ownershipPercentage: BigDecimal
)
