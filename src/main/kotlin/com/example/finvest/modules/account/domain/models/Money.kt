package com.example.finvest.modules.account.domain.models

import com.example.finvest.modules.account.domain.valueobject.CurrencyCode
import java.math.BigDecimal

data class Money(
    val amount: BigDecimal,
    val currencyCode: CurrencyCode,
)
