package com.example.finvest.modules.account.presentation.dto

import java.math.BigDecimal
import java.time.LocalDate

data class DetailsDtoPeaDto(
    val openingDate: LocalDate,
    val depositLimit: BigDecimal,
) : AccountDetailsDto
