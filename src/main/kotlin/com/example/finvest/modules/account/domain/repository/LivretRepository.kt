package com.example.finvest.modules.account.domain.repository

import com.example.finvest.modules.account.domain.models.Livret
import com.example.finvest.modules.account.domain.valueobject.AccountId

interface LivretRepository {
    fun getLivretByUserId(userId: Long): List<Livret>

    fun createLivret(livret: Livret): AccountId

    fun updateLivret(livret: Livret): Livret

    fun deleteLivret(accountId: AccountId)
}
