package com.example.finvest.modules.account.infrastructure.persistence.mapper


import com.example.finvest.modules.account.domain.models.*
import com.example.finvest.modules.account.infrastructure.persistence.models.*
import com.example.finvest.modules.common.domain.Currency

fun AccountDashboardRow.toDomain(): AccountDashboardData {
    return AccountDashboardData(
        account = Account(
            id = accountId,
            bankId = accountBankId,
            name = accountName,
            balance = accountBalance,
            currencyId = accountCurrencyId,
            createdAt = accountCreatedAt,
            closedAt = accountClosedAt,
            accountStatusId = accountStatusId,
            description = accountDescription,
            accountType = accountType
        ),

        bank = Bank(
            id = bankId,
            name = bankName,
            bic = bankBic,
            logo = bankLogo
        ),

        currency = Currency(
            id = currencyId,
            code = currencyCode,
        ),

        status = AccountStatus.valueOf(statusCode),

        owners = listOf(
            AccountOwner(
                id = ownerId,
                accountId = ownerAccountId,
                userId = ownerUserId,
                name = ownerName,
                ownershipPercentage = ownerOwnershipPercentage
            )
        )
    )
}

fun AssuranceVieEntity.toDomain(): AssuranceVie {
    return AssuranceVie(
        accountId = accountId,
        contractNumber = contractNumber,
        openingDate = openingDate,
        managementTypeId = managementTypeId
    )
}

fun CompteCourantEntity.toDomain(): CompteCourant {
    return CompteCourant(
        accountId = accountId,
        iban = iban,
        bic = bic,
        accountNumber = accountNumber,
        overdraftLimit = overdraftLimit,
        holderName = holderName,
    )
}

fun CompteTitreEntity.toDomain(): CompteTitre {
    return CompteTitre(
        accountId = accountId,
        accountNumber = accountNumber,
    )
}

fun LivretEntity.toDomain(): Livret {
    return Livret(
        accountId = accountId,
        interestRate = interestRate,
        ceiling = ceiling
    )
}

fun PeaEntity.toDomain(): Pea {
    return Pea(
        accountId = accountId,
        openingDate = openingDate,
        depositLimit = depositLimit
    )
}

fun PeeEntity.toDomain(): Pee {
    return Pee(
        accountId = accountId,
        openingDate = openingDate,
        employer = employer
    )
}

fun PerEntity.toDomain(): Per {
    return Per(
        accountId = accountId,
        contractNumber = contractNumber,
        openingDate = openingDate,
        managementTypeId = managementTypeId
    )
}