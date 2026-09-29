package com.example.finvest.modules.account.application.solver

import com.example.finvest.modules.account.domain.cache.ReferenceDataCache
import com.example.finvest.modules.account.domain.models.Account
import com.example.finvest.modules.account.domain.models.AccountWithDetails
import com.example.finvest.modules.account.domain.models.Per
import com.example.finvest.modules.account.domain.repository.PerRepository

class DashboardPerRepositorySolver(
    private val perRepository: PerRepository,
    private val referenceDataCache: ReferenceDataCache,
) : DashboardRepositorySolver<Per> {
    override fun solve(
        userId: Long,
        accountList: List<Account>,
    ): List<AccountWithDetails<Per>> {
        if (accountList.none {
                referenceDataCache.typeOf(it.accountTypeId.value).value == "PER"
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
