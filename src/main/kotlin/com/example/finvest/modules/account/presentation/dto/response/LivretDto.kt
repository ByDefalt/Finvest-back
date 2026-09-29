package com.example.finvest.modules.account.presentation.dto.response

import java.math.BigDecimal

data class LivretDto(
    val interestRate: BigDecimal,
    val ceiling: BigDecimal,
) : AccountDetailsDto
