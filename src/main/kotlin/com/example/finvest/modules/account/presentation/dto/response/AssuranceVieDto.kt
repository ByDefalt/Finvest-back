package com.example.finvest.modules.account.presentation.dto.response

import java.time.LocalDate

data class AssuranceVieDto(
    val contractNumber: String,
    val openingDate: LocalDate,
    val managementTypeId: Long,
) : AccountDetailsDto
