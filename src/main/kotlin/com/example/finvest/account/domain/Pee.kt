package com.example.finvest.account.domain

import java.time.LocalDate

data class Pee(
    var accountId: Long,
    var openingDate: LocalDate?,
    var employer: String?
)
