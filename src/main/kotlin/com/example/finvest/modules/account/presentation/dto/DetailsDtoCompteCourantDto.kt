package com.example.finvest.modules.account.presentation.dto

import java.math.BigDecimal

data class DetailsDtoCompteCourantDto(
    val iban: String,
    val bic: String,
    val accountNumber: String,
    val overdraftLimit: BigDecimal,
    val holderName: String,
) : AccountDetailsDto
