package com.example.finvest.modules.account.presentation.dto

import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class AccountOwnerDashboardResponse(
    val email: String?,
    val name: String?,
    @field:NotNull
    val ownershipPercentage: BigDecimal
)
