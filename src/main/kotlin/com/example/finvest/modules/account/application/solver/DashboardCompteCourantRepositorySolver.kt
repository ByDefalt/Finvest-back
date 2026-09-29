package com.example.finvest.modules.account.application.solver

import com.example.finvest.modules.account.domain.cache.ReferenceDataCache
import com.example.finvest.modules.account.domain.models.Account
import com.example.finvest.modules.account.domain.models.AccountWithDetails
import com.example.finvest.modules.account.domain.models.CompteCourant
import com.example.finvest.modules.account.domain.repository.CompteCourantRepository

class DashboardCompteCourantRepositorySolver(
    private val compteCourantRepository: CompteCourantRepository,
    private val referenceDataCache: ReferenceDataCache,
) : DashboardRepositorySolver<CompteCourant> {
    override fun solve(
        userId: Long,
        accountList: List<Account>,
    ): List<AccountWithDetails<CompteCourant>> {
        if (accountList.none {
                referenceDataCache.typeOf(it.accountTypeId.value).value == "COMPTE_COURANT"
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
