package com.example.finvest.modules.account.domain.models

import com.example.finvest.modules.account.domain.valueobject.AccountId
import java.time.LocalDate

data class Pee(
    val accountId: AccountId,
    val openingDate: LocalDate,
    val employer: String,
) : AccountDetails
