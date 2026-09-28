package com.example.finvest.modules.account.domain.repository

import com.example.finvest.modules.account.domain.models.AssuranceVie
import com.example.finvest.modules.account.domain.valueobject.AccountId

interface AssuranceVieRepository {
    fun getAssuranceVieByUserId(userId: Long): List<AssuranceVie>

    fun createAssuranceVie(assuranceVie: AssuranceVie): AccountId

    fun updateAssuranceVie(assuranceVie: AssuranceVie): AssuranceVie

    fun deleteAssuranceVie(accountId: AccountId)
}
