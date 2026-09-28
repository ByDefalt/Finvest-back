package com.example.finvest.modules.account.domain.repository

import com.example.finvest.modules.account.domain.valueobject.CurrencyCode

interface CurrencyRepository {
    fun getCurrencies(): Map<Long, CurrencyCode>
}
