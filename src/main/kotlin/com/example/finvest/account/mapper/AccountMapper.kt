package com.example.finvest.account.mapper

import com.example.finvest.account.domain.Account
import com.example.finvest.account.dto.AccountResponse
import com.example.finvest.account.entity.AccountEntity

fun AccountEntity.toDomain(): Account {
    return Account(
        id = id,
        bankId = bankId,
        name = name,
        balance = balance,
        currencyId = currencyId,
        createdAt = createdAt,
        closedAt = closedAt,
        accountStatusId = accountStatusId,
        description = description
    )
}

fun Account.toDto(): AccountResponse {
    return AccountResponse(
        id = id,
        bankId = bankId,
        name = name,
        balance = balance,
        currencyId = currencyId,
        createdAt = createdAt,
        closedAt = closedAt,
        accountStatusId = accountStatusId,
        description = description
    )
}

fun List<Account>.toDto(): List<AccountResponse> {
    return map { it.toDto() }
}

fun List<AccountEntity>.toDomain(): List<Account> {
    return map { it.toDomain() }
}

fun Account.toEntity(): AccountEntity {
    return AccountEntity(
        id = id,
        bankId = bankId,
        name = name,
        balance = balance,
        currencyId = currencyId,
        createdAt = createdAt,
        closedAt = closedAt,
        accountStatusId = accountStatusId,
        description = description
    )
}