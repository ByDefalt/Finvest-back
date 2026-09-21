package com.example.finvest.modules.account.domain.models

import java.time.LocalDate

data class Pee(
    var accountId: Long,
    var openingDate: LocalDate?,
    var employer: String?
)
