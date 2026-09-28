package com.example.finvest.modules.account.domain.repository

import com.example.finvest.modules.account.domain.models.Per
import com.example.finvest.modules.account.domain.valueobject.AccountId

interface PerRepository {
    fun getPerByUserId(userId: Long): List<Per>

    fun createPer(per: Per): AccountId

    fun updatePer(per: Per): Per

    fun deletePer(accountId: AccountId)
}
