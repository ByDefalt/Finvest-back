package com.example.finvest.modules.account.domain.repository

import com.example.finvest.modules.account.domain.models.CompteTitre
import com.example.finvest.modules.account.domain.valueobject.AccountId

interface CompteTitreRepository {
    fun getCompteTitreByUserId(userId: Long): List<CompteTitre>

    fun createCompteTitre(compteTitre: CompteTitre): AccountId

    fun updateCompteTitre(compteTitre: CompteTitre): CompteTitre

    fun deleteCompteTitre(accountId: AccountId)
}
