package com.example.finvest.modules.account.application.solver

import com.example.finvest.modules.account.domain.models.Account
import com.example.finvest.modules.account.domain.models.AccountDetails
import com.example.finvest.modules.account.domain.models.AccountWithDetails

interface DashboardRepositorySolver<out T : AccountDetails> {
    fun solve(
        userId: Long,
        accountList: List<Account>,
    ): List<AccountWithDetails<T>>
}
