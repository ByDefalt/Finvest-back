package com.example.finvest.modules.account.domain.repository

import com.example.finvest.modules.account.domain.models.AssuranceVie

interface AssuranceVieRepository {

    fun getAssuranceVieByUserId(userId: Long): List<AssuranceVie>
}
