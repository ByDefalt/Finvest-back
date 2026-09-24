package com.example.finvest.modules.account.domain.repository

import com.example.finvest.modules.account.domain.models.Livret

interface LivretRepository {

    fun getLivretByUserId(userId: Long): List<Livret>
}
