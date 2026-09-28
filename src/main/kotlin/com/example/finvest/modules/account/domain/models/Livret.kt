package com.example.finvest.modules.account.domain.models

import com.example.finvest.modules.account.domain.valueobject.AccountId
import java.math.BigDecimal

data class Livret(
    val accountId: AccountId,
    val interestRate: BigDecimal,
    val ceiling: BigDecimal,
) : AccountDetails
