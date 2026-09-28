package com.example.finvest.modules.account.domain.repository

import com.example.finvest.modules.account.domain.models.CompteCourant
import com.example.finvest.modules.account.domain.valueobject.AccountId

interface CompteCourantRepository {
    fun getCompteCourantByUserId(userId: Long): List<CompteCourant>

    fun createCompteCourant(compteCourant: CompteCourant): AccountId

    fun updateCompteCourant(compteCourant: CompteCourant): CompteCourant

    fun deleteCompteCourant(accountId: AccountId)
}
