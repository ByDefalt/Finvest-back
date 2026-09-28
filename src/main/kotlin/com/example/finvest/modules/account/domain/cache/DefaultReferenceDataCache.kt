package com.example.finvest.modules.account.domain.cache

import com.example.finvest.modules.account.domain.models.AccountStatus
import com.example.finvest.modules.account.domain.models.Bank
import com.example.finvest.modules.account.domain.valueobject.AccountType
import com.example.finvest.modules.account.domain.valueobject.CurrencyCode

class DefaultReferenceDataCache : ReferenceDataCache {
    @Volatile
    private var snapshot: ReferenceDataCache.Snapshot? = null

    override fun statusOf(id: Long): AccountStatus = current().statuses.getValue(id)

    override fun statusIdOf(code: String): Long =
        current()
            .statuses.entries
            .first { it.value.name == code }
            .key

    override fun typeOf(id: Long): AccountType = current().types.getValue(id)

    override fun typeIdOf(code: String): Long =
        current()
            .types.entries
            .first { it.value.value == code }
            .key

    override fun currencyOf(id: Long): CurrencyCode = current().currencies.getValue(id)

    override fun currencyIdOf(code: String): Long =
        current()
            .currencies.entries
            .first { it.value.value == code }
            .key

    override fun bankOf(id: Long): Bank = current().banks.getValue(id)

    override fun reload(newSnapshot: ReferenceDataCache.Snapshot) {
        snapshot = newSnapshot
    }

    private fun current(): ReferenceDataCache.Snapshot =
        snapshot ?: error("ReferenceDataCache non initialisé — reload() n'a jamais été appelé")
}
