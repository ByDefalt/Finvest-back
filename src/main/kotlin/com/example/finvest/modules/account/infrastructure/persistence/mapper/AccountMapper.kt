package com.example.finvest.modules.account.infrastructure.persistence.mapper


import com.example.finvest.modules.account.domain.models.*
import com.example.finvest.modules.account.infrastructure.persistence.models.AccountDashboardRow
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