package com.example.finvest.account.domain

import java.time.LocalDate

data class Per(
    var accountId: Long,
    var contractNumber: String?,
    var openingDate: LocalDate?,
    var managementTypeId: Long?
)
