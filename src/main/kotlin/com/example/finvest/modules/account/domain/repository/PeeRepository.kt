package com.example.finvest.modules.account.domain.repository

import com.example.finvest.modules.account.domain.models.Pee
import com.example.finvest.modules.account.domain.valueobject.AccountId

interface PeeRepository {
    fun getPeeByUserId(userId: Long): List<Pee>

    fun createPee(pee: Pee): AccountId

    fun updatePee(pee: Pee): Pee

    fun deletePee(accountId: AccountId)
}
