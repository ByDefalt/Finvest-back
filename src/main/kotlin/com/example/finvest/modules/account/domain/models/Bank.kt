package com.example.finvest.modules.account.domain.models

import com.example.finvest.modules.account.domain.valueobject.BankId
import com.example.finvest.modules.account.domain.valueobject.Bic

data class Bank(
    val id: BankId,
    val name: String,
    val bic: Bic,
    val logo: String?,
)
