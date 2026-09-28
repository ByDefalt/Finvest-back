package com.example.finvest.modules.account.application.usecase

import com.example.finvest.modules.account.domain.cache.ReferenceDataCache
import com.example.finvest.modules.account.domain.models.AccountStatus
import com.example.finvest.modules.account.domain.models.Bank
import com.example.finvest.modules.account.domain.repository.AccountStatusRepository
import com.example.finvest.modules.account.domain.repository.AccountTypeRepository
import com.example.finvest.modules.account.domain.repository.BankRepository
import com.example.finvest.modules.account.domain.repository.CurrencyRepository
import com.example.finvest.modules.account.domain.valueobject.AccountType
import com.example.finvest.modules.account.domain.valueobject.CurrencyCode
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class LoadReferenceDataUseCaseTest {
    @Test
    fun `loads repository data into the reference cache`() {
        val statusRepository = mock(AccountStatusRepository::class.java)
        val typeRepository = mock(AccountTypeRepository::class.java)
        val currencyRepository = mock(CurrencyRepository::class.java)
        val bankRepository = mock(BankRepository::class.java)
        val cache = mock(ReferenceDataCache::class.java)
        val statuses = mapOf(1L to AccountStatus.ACTIVE)
        val types = mapOf(2L to AccountType("CURRENT"))
        val currencies = mapOf(3L to CurrencyCode("EUR"))
        val banks = emptyMap<Long, Bank>()
        `when`(statusRepository.getAccountStatuses()).thenReturn(statuses)
        `when`(typeRepository.getAccountTypes()).thenReturn(types)
        `when`(currencyRepository.getCurrencies()).thenReturn(currencies)
        `when`(bankRepository.getBanks()).thenReturn(banks)

        LoadReferenceDataUseCase(
            statusRepository,
            typeRepository,
            currencyRepository,
            bankRepository,
            cache,
        )()

        verify(cache).reload(ReferenceDataCache.Snapshot(statuses, types, currencies, banks))
    }
}
