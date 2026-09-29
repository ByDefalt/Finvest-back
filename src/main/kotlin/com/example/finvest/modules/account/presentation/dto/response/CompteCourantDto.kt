package com.example.finvest.modules.account.presentation.dto.response

import java.math.BigDecimal

data class CompteCourantDto(
    val iban: String,
    val bic: String,
    val accountNumber: String,
    val overdraftLimit: BigDecimal,
    val holderName: String,
) : AccountDetailsDto
