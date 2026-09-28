package com.example.finvest.modules.account.domain.models

import com.example.finvest.modules.account.domain.valueobject.AccountId
import java.time.LocalDate

data class AssuranceVie(
    val accountId: AccountId,
    val contractNumber: String,
    val openingDate: LocalDate,
    val managementTypeId: Long,
) : AccountDetails
