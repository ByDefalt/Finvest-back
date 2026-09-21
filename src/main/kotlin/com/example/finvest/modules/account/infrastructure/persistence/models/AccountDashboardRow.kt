package com.example.finvest.modules.account.infrastructure.persistence.models

import java.math.BigDecimal
import java.time.LocalDateTime

data class AccountDashboardRow(
    // Account
    val accountId: Long,
    val accountBankId: Long,
    val accountName: String,
    val accountBalance: BigDecimal,
    val accountCurrencyId: Long,
    val accountCreatedAt: LocalDateTime,
    val accountClosedAt: LocalDateTime?,
    val accountStatusId: Long,
    val accountDescription: String?,
    val accountType: String,

    // Bank
    val bankId: Long,
    val bankName: String,
    val bankBic: String?,
    val bankLogo: String?,

    // Currency
    val currencyId: Long,
    val currencyCode: String,

    // AccountStatus
    val statusId: Long,
    val statusCode: String,

    // AccountOwner
    val ownerId: Long,
    val ownerAccountId: Long,
    val ownerUserId: Long?,
    val ownerName: String,
    val ownerOwnershipPercentage: BigDecimal,
)