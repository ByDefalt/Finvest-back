package com.example.finvest.account.domain

import com.example.finvest.common.domain.Currency

data class AccountDashboardData(
    val account: Account,
    val bank: Bank,
    val currency: Currency,
    val status: AccountStatus,
    val owners: List<AccountOwner>
)