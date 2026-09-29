package com.example.finvest.modules.account.application.solver

import com.example.finvest.modules.account.domain.models.Account
import com.example.finvest.modules.account.domain.models.AccountWithDetails
import com.example.finvest.modules.account.domain.models.CompteCourant
import com.example.finvest.modules.account.domain.repository.CompteCourantRepository

class DashboardCompteCourantRepositorySolver(
    private val compteCourantRepository: CompteCourantRepository,
) : DashboardRepositorySolver<CompteCourant> {
    override fun solve(
        userId: Long,
        accountList: List<Account>,
    ): List<AccountWithDetails<CompteCourant>> {
        if (accountList.none {
                it.accountType.value == "COMPTE_COURANT"
            }
        ) {
            return emptyList()
        }
        val compteCourantList = compteCourantRepository.getCompteCourantByUserId(userId)
        return accountList.mapNotNull { account ->
            compteCourantList.find { it.accountId.value == account.id.value }?.let {
                AccountWithDetails(account, it)
            }
        }
    }
}
