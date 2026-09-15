package com.example.finvest.account.domain

import java.math.BigDecimal

data class AccountOwner(
    var id: Long,
    var accountId: Long,
    var userId: Long?,
    var name: String,
    var ownershipPercentage: BigDecimal
)
