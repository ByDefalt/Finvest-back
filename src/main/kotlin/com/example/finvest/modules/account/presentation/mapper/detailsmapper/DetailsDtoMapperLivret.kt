package com.example.finvest.modules.account.presentation.mapper.detailsmapper

import com.example.finvest.modules.account.domain.cache.ReferenceDataCache
import com.example.finvest.modules.account.domain.models.AccountWithDetails
import com.example.finvest.modules.account.presentation.dto.response.AccountWithDetailsDto
import com.example.finvest.modules.account.presentation.mapper.toDto
import org.springframework.stereotype.Component

@Component
class DetailsDtoMapperLivret(
    private val referenceDataCache: ReferenceDataCache,
) : DetailsDtoMapper {
    @Suppress("UNCHECKED_CAST")
    override fun mapToDto(accountDetails: AccountWithDetails<*>): AccountWithDetailsDto? {
        if (accountDetails.account.accountType.value != "LIVRET") return null
        return (accountDetails as AccountWithDetails<com.example.finvest.modules.account.domain.models.Livret>).toDto(referenceDataCache)
    }
}
