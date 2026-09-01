package com.example.finvest.account.mapper

import com.example.finvest.account.domain.Account
import com.example.finvest.account.domain.AccountType
import com.example.finvest.account.dto.AccountResponse
import com.example.finvest.account.entity.AccountEntity

fun AccountEntity.toDomain(): Account {
    return Account(
        id = requireNotNull(this.id),
        name = this.name,
        balance = this.balance,
        type = AccountType.valueOf(this.type),
        userId = this.userId,
    )
}

fun Account.toDto(): AccountResponse {
    return AccountResponse(
        id = this.id,
        name = this.name,
        balance = this.balance,
        type = this.type.name,
    )
}

fun List<Account>.toDto(): List<AccountResponse> {
    return this.map { it.toDto() }
}

fun  List<AccountEntity>.toDomain(): List<Account> {
    return this.map { it.toDomain() }
}

fun Account.toEntity(): AccountEntity {
    return AccountEntity(
        id = this.id,
        name = this.name,
        balance = this.balance,
        type = this.type.name,
        userId = this.userId
    )
}