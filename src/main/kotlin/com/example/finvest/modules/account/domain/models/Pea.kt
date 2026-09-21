package com.example.finvest.modules.account.domain.models

import java.math.BigDecimal
import java.time.LocalDate

data class Pea(
    var accountId: Long,
    var openingDate: LocalDate?,
    var depositLimit: BigDecimal?
)
