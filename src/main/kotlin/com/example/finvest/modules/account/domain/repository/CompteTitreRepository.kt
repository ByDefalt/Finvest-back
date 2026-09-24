package com.example.finvest.modules.account.domain.repository

import com.example.finvest.modules.account.domain.models.CompteTitre

interface CompteTitreRepository {

    fun getCompteTitreByUserId(userId: Long): List<CompteTitre>
}
