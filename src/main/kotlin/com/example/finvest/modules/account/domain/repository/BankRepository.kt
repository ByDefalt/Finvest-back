package com.example.finvest.modules.account.domain.repository

import com.example.finvest.modules.account.domain.models.Bank

interface BankRepository {
    fun getBanks(): Map<Long, Bank>
}
