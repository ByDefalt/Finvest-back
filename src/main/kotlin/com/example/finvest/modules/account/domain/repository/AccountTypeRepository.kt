package com.example.finvest.modules.account.domain.repository

import com.example.finvest.modules.account.domain.valueobject.AccountType

interface AccountTypeRepository {
    fun getAccountTypes(): Map<Long, AccountType>
}
