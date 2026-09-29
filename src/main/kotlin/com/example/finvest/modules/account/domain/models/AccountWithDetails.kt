package com.example.finvest.modules.account.domain.models

data class AccountWithDetails<out T : AccountDetails>(
    val account: Account,
    val details: T,
)
