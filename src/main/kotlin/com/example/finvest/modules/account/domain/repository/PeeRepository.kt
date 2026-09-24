package com.example.finvest.modules.account.domain.repository

import com.example.finvest.modules.account.domain.models.Pee

interface PeeRepository {

    fun getPeeByUserId(userId: Long): List<Pee>
}
