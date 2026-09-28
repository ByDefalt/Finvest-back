package com.example.finvest.modules.account.domain.models

import com.example.finvest.modules.account.domain.valueobject.CurrencyId
import java.math.BigDecimal

data class Money(
    val amount: BigDecimal,
    val currencyId: CurrencyId,
)
