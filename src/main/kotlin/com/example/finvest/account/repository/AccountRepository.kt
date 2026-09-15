package com.example.finvest.account.repository

import com.example.finvest.account.domain.Account

interface AccountRepository {

    fun findAllByUserId(userId: Long): List<Account>

    fun createAccount(account: Account): Account

    fun updateAccount(account: Account, userId: Long): Account

    fun deleteAccount(id: Long, userId: Long)
    fun findByIdAndUserId(id: Long, userId: Long): Account
}
