package com.example.finvest.modules.account.domain.repository

import com.example.finvest.modules.account.domain.models.AccountStatus

interface AccountStatusRepository {
    fun getAccountStatuses(): Map<Long, AccountStatus>
}
