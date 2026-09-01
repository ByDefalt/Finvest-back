package com.example.finvest.account.repository

import com.example.finvest.account.domain.Account

interface AccountRepository {

    fun findAllByUserId(userId: Long): List<Account>

    fun createAccount(account: Account): Account

    fun updateAccount(account: Account): Account

    fun deleteAccount(id: Long, userId: Long)
}
