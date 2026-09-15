package com.example.finvest.account.mapper

import com.example.finvest.account.domain.Account
import com.example.finvest.account.domain.AccountDashboardData
import com.example.finvest.account.domain.AccountOwner
import com.example.finvest.account.domain.Bank
import com.example.finvest.account.dto.AccountDashboardResponse
import com.example.finvest.account.dto.AccountOwnerDashboardResponse
import com.example.finvest.account.dto.AccountStatusDto as AccountStatusDto
import com.example.finvest.account.domain.AccountStatus as AccountStatusDomain
import com.example.finvest.account.entity.AccountDashboardRow
import com.example.finvest.auth.domain.User
import com.example.finvest.common.domain.Currency

fun List<AccountDashboardRow>.toDomain(): List<AccountDashboardData> {
    return groupBy { it.accountId }
        .map { (_, rows) ->
            val first = rows.first()

            AccountDashboardData(
                account = Account(
                    id = first.accountId,
                    bankId = first.accountBankId,
                    name = first.accountName,
                    balance = first.accountBalance,
                    currencyId = first.accountCurrencyId,
                    createdAt = first.accountCreatedAt,
                    closedAt = first.accountClosedAt,
                    accountStatusId = first.accountStatusId,
                    description = first.accountDescription,
                    accountType = first.accountType
                ),

                bank = Bank(
                    id = first.bankId,
                    name = first.bankName,
                    bic = first.bankBic,
                    logo = first.bankLogo
                ),

                currency = Currency(
                    id = first.currencyId,
                    code = first.currencyCode,
                    name = first.currencyName,
                    symbol = first.currencySymbol
                ),

                status = AccountStatusDomain(
                    id = first.statusId,
                    code = first.statusCode,
                    name = first.statusName
                ),

                owners = rows.map { row ->
                    AccountOwner(
                        id = row.ownerId,
                        accountId = row.ownerAccountId,
                        userId = row.ownerUserId,
                        name = row.ownerName,
                        ownershipPercentage = row.ownerOwnershipPercentage
                    )
                }
            )
        }
}

fun AccountDashboardData.toResponse(user: User): AccountDashboardResponse {
    return AccountDashboardResponse(
        id = account.id,
        bankName = bank.name,
        name = account.name,
        balance = account.balance,
        currency = currency.code,
        createdAt = account.createdAt,
        closedAt = account.closedAt,
        accountStatus = AccountStatusDto.valueOf(status.code),
        description = account.description,
        type = account.accountType,
        accountOwners = owners.map { owner ->
            AccountOwnerDashboardResponse(
                email = user.email,
                name = owner.name,
                ownershipPercentage = owner.ownershipPercentage
            )
        }
    )
}