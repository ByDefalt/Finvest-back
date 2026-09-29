package com.example.finvest.modules.account.application.solver

import com.example.finvest.modules.account.domain.cache.ReferenceDataCache
import com.example.finvest.modules.account.domain.models.Account
import com.example.finvest.modules.account.domain.models.AccountWithDetails
import com.example.finvest.modules.account.domain.models.Livret
import com.example.finvest.modules.account.domain.repository.LivretRepository

class DashboardLivretRepositorySolver(
    private val livretRepository: LivretRepository,
    private val referenceDataCache: ReferenceDataCache,
) : DashboardRepositorySolver<Livret> {
    override fun solve(
        userId: Long,
        accountList: List<Account>,
    ): List<AccountWithDetails<Livret>> {
        if (accountList.none {
                referenceDataCache.typeOf(it.accountTypeId.value).value == "LIVRET"
            }
        ) {
            return emptyList()
        }
        val livretList = livretRepository.getLivretByUserId(userId)
        return accountList.mapNotNull { account ->
            livretList.find { it.accountId.value == account.id.value }?.let {
                AccountWithDetails(account, it)
            }
        }
    }
}
