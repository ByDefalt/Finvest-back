package com.example.finvest.modules.account.domain.models

import java.math.BigDecimal

data class AccountOwner(
    var id: Long,
    var accountId: Long,
    var userId: Long?,
    var name: String,
    var ownershipPercentage: BigDecimal
)
