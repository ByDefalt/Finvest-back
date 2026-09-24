package com.example.finvest.modules.account.domain.repository

import com.example.finvest.modules.account.domain.models.Per

interface PerRepository {

    fun getPerByUserId(userId: Long): List<Per>
}
