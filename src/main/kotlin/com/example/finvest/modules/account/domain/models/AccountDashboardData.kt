package com.example.finvest.modules.account.domain.models

import com.example.finvest.modules.common.domain.Currency

data class AccountDashboardData(
    val account: Account,
    val bank: Bank,
    val currency: Currency,
    val status: AccountStatus,
    val owners: List<AccountOwner>
)