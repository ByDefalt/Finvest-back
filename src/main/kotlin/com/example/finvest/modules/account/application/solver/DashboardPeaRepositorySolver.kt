package com.example.finvest.modules.account.application.solver

import com.example.finvest.modules.account.domain.models.Account
import com.example.finvest.modules.account.domain.models.AccountWithDetails
import com.example.finvest.modules.account.domain.models.Pea
import com.example.finvest.modules.account.domain.repository.PeaRepository

class DashboardPeaRepositorySolver(
    private val peaRepository: PeaRepository,
) : DashboardRepositorySolver<Pea> {
    override fun solve(
        userId: Long,
        accountList: List<Account>,
    ): List<AccountWithDetails<Pea>> {
        if (accountList.none {
                it.accountType.value == "PEA"
            }
        ) {
            return emptyList()
        }
        val peaList = peaRepository.getPeaByUserId(userId)
        return accountList.mapNotNull { account ->
            peaList.find { it.accountId.value == account.id.value }?.let {
                AccountWithDetails(account, it)
            }
        }
    }
}
