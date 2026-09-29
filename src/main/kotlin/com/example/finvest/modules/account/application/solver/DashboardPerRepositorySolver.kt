package com.example.finvest.modules.account.application.solver

import com.example.finvest.modules.account.domain.models.Account
import com.example.finvest.modules.account.domain.models.AccountWithDetails
import com.example.finvest.modules.account.domain.models.Per
import com.example.finvest.modules.account.domain.repository.PerRepository

class DashboardPerRepositorySolver(
    private val perRepository: PerRepository,
) : DashboardRepositorySolver<Per> {
    override fun solve(
        userId: Long,
        accountList: List<Account>,
    ): List<AccountWithDetails<Per>> {
        if (accountList.none {
                it.accountType.value == "PER"
            }
        ) {
            return emptyList()
        }
        val perList = perRepository.getPerByUserId(userId)
        return accountList.mapNotNull { account ->
            perList.find { it.accountId.value == account.id.value }?.let {
                AccountWithDetails(account, it)
            }
        }
    }
}
