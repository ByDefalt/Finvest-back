package com.example.finvest.modules.account.presentation.dto.response

data class AccountWithDetailsDto(
    val account: AccountDto,
    val details: AccountDetailsDto,
)
