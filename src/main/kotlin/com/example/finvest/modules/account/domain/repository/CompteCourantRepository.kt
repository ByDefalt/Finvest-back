package com.example.finvest.modules.account.domain.repository

import com.example.finvest.modules.account.domain.models.CompteCourant

interface CompteCourantRepository {
    fun getCompteCourantByUserId(userId: Long): List<CompteCourant>
}