package com.example.finvest.modules.account.presentation.dto

import java.math.BigDecimal

data class DetailsDtoLivretDto(
    val interestRate: BigDecimal,
    val ceiling: BigDecimal,
) : AccountDetailsDto
