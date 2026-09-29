package com.example.finvest.modules.account.application.solver

import com.example.finvest.modules.account.domain.cache.ReferenceDataCache
import com.example.finvest.modules.account.domain.models.Account
import com.example.finvest.modules.account.domain.models.AccountWithDetails
import com.example.finvest.modules.account.domain.models.Pea
import com.example.finvest.modules.account.domain.repository.PeaRepository

class DashboardPeaRepositorySolver(
    private val peaRepository: PeaRepository,
    private val referenceDataCache: ReferenceDataCache,
) : DashboardRepositorySolver<Pea> {
    override fun solve(
        userId: Long,
        accountList: List<Account>,
    ): List<AccountWithDetails<Pea>> {
        if (accountList.none {
                referenceDataCache.typeOf(it.accountTypeId.value).value == "PEA"
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
