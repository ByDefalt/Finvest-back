package com.example.finvest.modules.account.presentation.mapper.detailsmapper

import com.example.finvest.modules.account.domain.models.AccountWithDetails
import com.example.finvest.modules.account.presentation.dto.response.AccountWithDetailsDto

interface DetailsDtoMapper {
    fun mapToDto(accountDetails: AccountWithDetails<*>): AccountWithDetailsDto?
}
