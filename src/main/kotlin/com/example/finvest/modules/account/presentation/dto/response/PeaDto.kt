package com.example.finvest.modules.account.presentation.dto.response

import java.math.BigDecimal
import java.time.LocalDate

data class PeaDto(
    val openingDate: LocalDate,
    val depositLimit: BigDecimal,
) : AccountDetailsDto
