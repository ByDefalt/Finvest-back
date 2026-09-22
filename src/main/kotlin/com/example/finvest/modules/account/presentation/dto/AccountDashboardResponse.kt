package com.example.finvest.modules.account.presentation.dto

import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import java.time.LocalDateTime

data class AccountDashboardResponse(
    @field:NotNull
    val id: Long,
    @field:NotNull
    val bankName: String,
    @field:NotNull
    val name: String,
    @field:NotNull
    val balance: BigDecimal,
    @field:NotNull
    val currency: String,
    @field:NotNull
    val createdAt: LocalDateTime,
    val closedAt: LocalDateTime?,
    @field:NotNull
    val accountStatus: AccountStatusDto,
    val description: String?,
    @field:NotNull
    val type: String,
    @field:NotNull
    val accountOwners: List<AccountOwnerDashboardResponse>
)
