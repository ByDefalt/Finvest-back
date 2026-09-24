package com.example.finvest.modules.account.domain.repository

import com.example.finvest.modules.account.domain.models.Pea

interface PeaRepository {

    fun getPeaByUserId(userId: Long): List<Pea>
}
