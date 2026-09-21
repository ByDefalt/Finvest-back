package com.example.finvest.modules.account.domain.models

import java.math.BigDecimal

data class CompteCourant(
    var accountId: Long,
    var iban: String?,
    var bic: String?,
    var accountNumber: String?,
    var overdraftLimit: BigDecimal?,
    var holderName: String?
)
