package com.example.finvest.modules.account.application.usecase.get

import com.example.finvest.modules.account.domain.cache.ReferenceDataCache
import com.example.finvest.modules.account.domain.repository.AccountStatusRepository
import com.example.finvest.modules.account.domain.repository.AccountTypeRepository
import com.example.finvest.modules.account.domain.repository.BankRepository
import com.example.finvest.modules.account.domain.repository.CurrencyRepository

class LoadReferenceDataUseCase(
    private val accountStatusRepository: AccountStatusRepository,
    private val accountTypeRepository: AccountTypeRepository,
    private val currencyRepository: CurrencyRepository,
    private val bankRepository: BankRepository,
    private val cache: ReferenceDataCache,
) {
    operator fun invoke() {
        cache.reload(
            ReferenceDataCache.Snapshot(
                statuses = accountStatusRepository.getAccountStatuses(),
                types = accountTypeRepository.getAccountTypes(),
                currencies = currencyRepository.getCurrencies(),
                banks = bankRepository.getBanks(),
            ),
        )
    }
}
