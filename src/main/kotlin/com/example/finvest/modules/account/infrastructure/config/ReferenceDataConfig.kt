package com.example.finvest.modules.account.infrastructure.config

import com.example.finvest.modules.account.application.usecase.get.LoadReferenceDataUseCase
import com.example.finvest.modules.account.domain.cache.DefaultReferenceDataCache
import com.example.finvest.modules.account.domain.cache.ReferenceDataCache
import com.example.finvest.modules.account.domain.repository.AccountStatusRepository
import com.example.finvest.modules.account.domain.repository.AccountTypeRepository
import com.example.finvest.modules.account.domain.repository.BankRepository
import com.example.finvest.modules.account.domain.repository.CurrencyRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ReferenceDataConfig {
    @Bean
    fun loadReferenceDataUseCase(
        accountStatusRepository: AccountStatusRepository,
        accountTypeRepository: AccountTypeRepository,
        currencyRepository: CurrencyRepository,
        bankRepository: BankRepository,
        cache: ReferenceDataCache,
    ): LoadReferenceDataUseCase =
        LoadReferenceDataUseCase(
            accountStatusRepository = accountStatusRepository,
            accountTypeRepository = accountTypeRepository,
            currencyRepository = currencyRepository,
            bankRepository = bankRepository,
            cache = cache,
        )

    @Bean
    fun referenceDataCache(): ReferenceDataCache = DefaultReferenceDataCache()
}
