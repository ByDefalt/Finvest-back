package com.example.finvest.account.repository

import com.example.finvest.account.domain.Account
import com.example.finvest.account.domain.AccountDashboardData

interface AccountRepository {

    fun findDashboardByUserId(userId: Long): List<AccountDashboardData>
}
