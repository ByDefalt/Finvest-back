package com.example.finvest.account.domain

import java.math.BigDecimal

data class Livret(
    var accountId: Long,
    var interestRate: BigDecimal?,
    var ceiling: BigDecimal?
)
