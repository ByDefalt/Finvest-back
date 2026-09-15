package com.example.finvest.account.service

import com.example.finvest.account.dto.AccountStatusDto
import com.example.finvest.account.dto.DashboardResponse
import com.example.finvest.account.mapper.toResponse
import com.example.finvest.account.repository.AccountRepository
import com.example.finvest.auth.domain.User
import org.springframework.stereotype.Service

@Service
class DashboardService(
    private val accountRepository: AccountRepository
) {

    fun getDashboard(user: User): DashboardResponse {
        val accountsDashboard =
            accountRepository.findDashboardByUserId(user.id)

        val accounts = accountsDashboard.map { it.toResponse(user) }

        return DashboardResponse(
            totalAccounts = accountsDashboard.size,
            totalBalance = accountsDashboard
                .sumOf { it.account.balance },

            totalActiveAccounts = accounts.count {
                it.accountStatus == AccountStatusDto.ACTIVE
            },

            accounts = accounts
        )
    }
}