package com.example.finvest.modules.account.domain.models

import java.time.LocalDate

data class AssuranceVie(
    var accountId: Long,
    var contractNumber: String?,
    var openingDate: LocalDate?,
    var managementTypeId: Long?
)
