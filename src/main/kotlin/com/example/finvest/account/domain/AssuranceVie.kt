package com.example.finvest.account.domain

import java.time.LocalDate

data class AssuranceVie(
    var accountId: Long,
    var contractNumber: String?,
    var openingDate: LocalDate?,
    var managementTypeId: Long?
)
