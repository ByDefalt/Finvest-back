package com.example.finvest.modules.account.domain.repository

import com.example.finvest.modules.account.domain.models.AccountDashboardData


interface AccountRepository {

    fun findDashboardByUserId(userId: Long): List<AccountDashboardData>
}
