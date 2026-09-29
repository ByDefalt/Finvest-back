package com.example.finvest.modules.account.application.solver

import com.example.finvest.modules.account.domain.cache.ReferenceDataCache
import com.example.finvest.modules.account.domain.models.Account
import com.example.finvest.modules.account.domain.models.AccountWithDetails
import com.example.finvest.modules.account.domain.models.AssuranceVie
import com.example.finvest.modules.account.domain.repository.AssuranceVieRepository

class DashboardAssuranceVieRepositorySolver(
    private val assuranceVieRepository: AssuranceVieRepository,
    private val referenceDataCache: ReferenceDataCache,
) : DashboardRepositorySolver<AssuranceVie> {
    override fun solve(
        userId: Long,
        accountList: List<Account>,
    ): List<AccountWithDetails<AssuranceVie>> {
        if (accountList.none {
                referenceDataCache.typeOf(it.accountTypeId.value).value == "ASSURANCE_VIE"
            }
        ) {
            return emptyList()
        }
        val assuranceVieList = assuranceVieRepository.getAssuranceVieByUserId(userId)
        return accountList.mapNotNull { account ->
            assuranceVieList.find { it.accountId.value == account.id.value }?.let {
                AccountWithDetails(account, it)
            }
        }
    }
}
