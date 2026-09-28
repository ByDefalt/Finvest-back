package com.example.finvest.modules.account.presentation.dto

import java.time.LocalDate

data class DetailsDtoAssuranceVieDto(
    val contractNumber: String,
    val openingDate: LocalDate,
    val managementTypeId: Long,
) : AccountDetailsDto
