package com.example.finvest.modules.account.domain.models

import com.example.finvest.modules.account.domain.valueobject.AccountId
import java.math.BigDecimal
import java.time.LocalDate

data class Pea(
    val accountId: AccountId,
    val openingDate: LocalDate,
    val depositLimit: BigDecimal,
) : AccountDetails
