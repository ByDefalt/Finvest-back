package com.example.finvest.modules.account.presentation.mapper

import com.example.finvest.modules.account.domain.models.AccountDashboardData
import com.example.finvest.modules.account.domain.models.AccountStatus
import com.example.finvest.modules.account.presentation.dto.AccountDashboardResponse
import com.example.finvest.modules.account.presentation.dto.AccountOwnerDashboardResponse
import com.example.finvest.modules.account.presentation.dto.AccountStatusDto
import com.example.finvest.modules.account.presentation.dto.DashboardResponse
import com.example.finvest.modules.shared.application.security.AuthenticatedUser

fun AccountDashboardData.toResponse(authenticatedUser: AuthenticatedUser): AccountDashboardResponse {
    return AccountDashboardResponse(
        id = this.account.id,
        bankName = this.bank.name,
        name = this.account.name,
        balance = this.account.balance,
        currency = this.currency.code,
        createdAt = this.account.createdAt,
        closedAt = this.account.closedAt,
        accountStatus = AccountStatusDto.valueOf(this.status.name),
        description = this.account.description,
        type = this.account.accountType,
        accountOwners = this.owners.map { owner ->
            AccountOwnerDashboardResponse(
                email = if(owner.userId == authenticatedUser.id) authenticatedUser.email else null,
                name = owner.name,
                ownershipPercentage = owner.ownershipPercentage
            )
        },
    )
}

fun List<AccountDashboardData>.toDashboardResponse(authenticatedUser: AuthenticatedUser): DashboardResponse {

    return DashboardResponse(
        totalAccounts = this.size,
        totalBalance = this.sumOf { it.account.balance },
        totalActiveAccounts = this.count { it.status == AccountStatus.ACTIVE },
        accounts = this.map { it.toResponse(authenticatedUser) }
    )
}