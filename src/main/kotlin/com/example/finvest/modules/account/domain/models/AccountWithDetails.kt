package com.example.finvest.modules.account.domain.models

data class AccountWithDetails<T : AccountDetails>(
    val account: Account,
    val details: T,
)
