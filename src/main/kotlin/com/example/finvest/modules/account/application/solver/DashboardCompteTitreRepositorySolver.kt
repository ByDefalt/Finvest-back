package com.example.finvest.modules.account.application.solver

import com.example.finvest.modules.account.domain.models.Account
import com.example.finvest.modules.account.domain.models.AccountWithDetails
import com.example.finvest.modules.account.domain.models.CompteTitre
import com.example.finvest.modules.account.domain.repository.CompteTitreRepository

class DashboardCompteTitreRepositorySolver(
    private val compteTitreRepository: CompteTitreRepository,
) : DashboardRepositorySolver<CompteTitre> {
    override fun solve(
        userId: Long,
        accountList: List<Account>,
    ): List<AccountWithDetails<CompteTitre>> {
        if (accountList.none {
                it.accountType.value == "COMPTE_TITRES"
            }
        ) {
            return emptyList()
        }
        val compteTitreList = compteTitreRepository.getCompteTitreByUserId(userId)
        return accountList.mapNotNull { account ->
            compteTitreList.find { it.accountId.value == account.id.value }?.let {
                AccountWithDetails(account, it)
            }
        }
    }
}
