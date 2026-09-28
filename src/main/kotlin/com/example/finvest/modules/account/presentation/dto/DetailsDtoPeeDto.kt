package com.example.finvest.modules.account.presentation.dto

import java.time.LocalDate

data class DetailsDtoPeeDto(
    val openingDate: LocalDate,
    val employer: String,
) : AccountDetailsDto
