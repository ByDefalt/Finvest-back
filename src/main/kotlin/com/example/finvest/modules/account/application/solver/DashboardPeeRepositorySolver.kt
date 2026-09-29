package com.example.finvest.modules.account.application.solver

import com.example.finvest.modules.account.domain.cache.ReferenceDataCache
import com.example.finvest.modules.account.domain.models.Account
import com.example.finvest.modules.account.domain.models.AccountWithDetails
import com.example.finvest.modules.account.domain.models.Pee
import com.example.finvest.modules.account.domain.repository.PeeRepository

class DashboardPeeRepositorySolver(
    private val peeRepository: PeeRepository,
    private val referenceDataCache: ReferenceDataCache,
) : DashboardRepositorySolver<Pee> {
    override fun solve(
        userId: Long,
        accountList: List<Account>,
    ): List<AccountWithDetails<Pee>> {
        if (accountList.none {
                referenceDataCache.typeOf(it.accountTypeId.value).value == "PEE"
            }
        ) {
            return emptyList()
        }
        val peeList = peeRepository.getPeeByUserId(userId)
        return accountList.mapNotNull { account ->
            peeList.find { it.accountId.value == account.id.value }?.let {
                AccountWithDetails(account, it)
            }
        }
    }
}
