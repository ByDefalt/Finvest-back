package com.example.finvest.modules.account.domain.cache

import com.example.finvest.modules.account.domain.models.AccountStatus
import com.example.finvest.modules.account.domain.models.Bank
import com.example.finvest.modules.account.domain.valueobject.AccountType
import com.example.finvest.modules.account.domain.valueobject.CurrencyCode

interface ReferenceDataCache {
    data class Snapshot(
        val statuses: Map<Long, AccountStatus>,
        val types: Map<Long, AccountType>,
        val currencies: Map<Long, CurrencyCode>,
        val banks: Map<Long, Bank>,
    )

    fun statusOf(id: Long): AccountStatus

    fun statusIdOf(code: String): Long

    fun typeOf(id: Long): AccountType

    fun typeIdOf(code: String): Long

    fun currencyOf(id: Long): CurrencyCode

    fun currencyIdOf(code: String): Long

    fun bankOf(id: Long): Bank

    fun bankOf(bic: String): Bank

    fun reload(newSnapshot: Snapshot)
}
