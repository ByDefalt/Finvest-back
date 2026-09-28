package com.example.finvest.modules.account.domain.repository

import com.example.finvest.modules.account.domain.models.Account
import com.example.finvest.modules.account.domain.valueobject.AccountId

interface AccountRepository {
    fun createAccount(account: Account): AccountId

    fun updateAccount(account: Account): Account

    fun deleteAccount(accountId: AccountId)
}
