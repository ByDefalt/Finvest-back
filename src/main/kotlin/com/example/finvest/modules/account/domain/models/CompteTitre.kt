package com.example.finvest.modules.account.domain.models

import com.example.finvest.modules.account.domain.valueobject.AccountId

data class CompteTitre(
    val accountId: AccountId,
    val accountNumber: String,
) : AccountDetails
