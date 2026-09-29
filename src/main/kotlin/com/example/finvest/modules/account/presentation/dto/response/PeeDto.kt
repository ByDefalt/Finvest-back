package com.example.finvest.modules.account.presentation.dto.response

import java.time.LocalDate

data class PeeDto(
    val openingDate: LocalDate,
    val employer: String,
) : AccountDetailsDto
