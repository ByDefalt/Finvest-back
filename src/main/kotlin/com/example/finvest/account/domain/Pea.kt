package com.example.finvest.account.domain

import java.math.BigDecimal
import java.time.LocalDate

data class Pea(
    var accountId: Long,
    var openingDate: LocalDate?,
    var depositLimit: BigDecimal?
)
