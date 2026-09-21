package com.example.finvest.modules.account.domain.models

import java.math.BigDecimal

data class Livret(
    var accountId: Long,
    var interestRate: BigDecimal?,
    var ceiling: BigDecimal?
)
