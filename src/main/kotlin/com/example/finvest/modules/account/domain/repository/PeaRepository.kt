package com.example.finvest.modules.account.domain.repository

import com.example.finvest.modules.account.domain.models.Pea
import com.example.finvest.modules.account.domain.valueobject.AccountId

interface PeaRepository {
    fun getPeaByUserId(userId: Long): List<Pea>

    fun createPea(pea: Pea): AccountId

    fun updatePea(pea: Pea): Pea

    fun deletePea(accountId: AccountId)
}
