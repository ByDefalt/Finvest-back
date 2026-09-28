package com.example.finvest.modules.account.domain.models

import com.example.finvest.modules.account.domain.valueobject.AccountId
import com.example.finvest.modules.account.domain.valueobject.AccountOwnerId
import java.math.BigDecimal

data class AccountOwner(
    val id: AccountOwnerId,
    val accountId: AccountId,
    val userId: Long?,
    val name: String,
    val ownershipPercentage: BigDecimal,
)
