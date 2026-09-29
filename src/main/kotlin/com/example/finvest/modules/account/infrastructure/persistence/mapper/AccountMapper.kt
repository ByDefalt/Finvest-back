package com.example.finvest.modules.account.infrastructure.persistence.mapper

import com.example.finvest.modules.account.domain.cache.ReferenceDataCache
import com.example.finvest.modules.account.domain.models.Account
import com.example.finvest.modules.account.domain.models.AssuranceVie
import com.example.finvest.modules.account.domain.models.CompteCourant
import com.example.finvest.modules.account.domain.models.CompteTitre
import com.example.finvest.modules.account.domain.models.Livret
import com.example.finvest.modules.account.domain.models.Money
import com.example.finvest.modules.account.domain.models.Pea
import com.example.finvest.modules.account.domain.models.Pee
import com.example.finvest.modules.account.domain.models.Per
import com.example.finvest.modules.account.domain.valueobject.AccountId
import com.example.finvest.modules.account.domain.valueobject.AccountStatusId
import com.example.finvest.modules.account.domain.valueobject.AccountTypeId
import com.example.finvest.modules.account.domain.valueobject.BankId
import com.example.finvest.modules.account.domain.valueobject.CurrencyId
import com.example.finvest.modules.account.infrastructure.persistence.models.AccountEntity
import com.example.finvest.modules.account.infrastructure.persistence.models.AssuranceVieEntity
import com.example.finvest.modules.account.infrastructure.persistence.models.CompteCourantEntity
import com.example.finvest.modules.account.infrastructure.persistence.models.CompteTitreEntity
import com.example.finvest.modules.account.infrastructure.persistence.models.LivretEntity
import com.example.finvest.modules.account.infrastructure.persistence.models.PeaEntity
import com.example.finvest.modules.account.infrastructure.persistence.models.PeeEntity
import com.example.finvest.modules.account.infrastructure.persistence.models.PerEntity

fun AccountEntity.toDomain(referenceDataCache: ReferenceDataCache): Account =
    Account(
        id = AccountId(id),
        bank = referenceDataCache.bankOf(bankId),
        name = name,
        balance =
            Money(
                amount = balance,
                currencyCode = referenceDataCache.currencyOf(currencyId),
            ),
        createdAt = createdAt,
        closedAt = closedAt,
        description = description,
        accountStatus = referenceDataCache.statusOf(accountStatusId),
        accountType = referenceDataCache.typeOf(accountTypeId),
    )

fun AssuranceVieEntity.toDomain(): AssuranceVie =
    AssuranceVie(
        accountId = accountId.toAccountId(),
        contractNumber = contractNumber,
        openingDate = openingDate,
        managementTypeId = managementTypeId,
    )

fun CompteCourantEntity.toDomain(): CompteCourant =
    CompteCourant(
        accountId = accountId.toAccountId(),
        iban = iban,
        bic = bic,
        accountNumber = accountNumber,
        overdraftLimit = overdraftLimit,
        holderName = holderName,
    )

fun CompteTitreEntity.toDomain(): CompteTitre =
    CompteTitre(
        accountId = accountId.toAccountId(),
        accountNumber = accountNumber,
    )

fun LivretEntity.toDomain(): Livret =
    Livret(
        accountId = accountId.toAccountId(),
        interestRate = interestRate,
        ceiling = ceiling,
    )

fun PeaEntity.toDomain(): Pea =
    Pea(
        accountId = accountId.toAccountId(),
        openingDate = openingDate,
        depositLimit = depositLimit,
    )

fun PeeEntity.toDomain(): Pee =
    Pee(
        accountId = accountId.toAccountId(),
        openingDate = openingDate,
        employer = employer,
    )

fun PerEntity.toDomain(): Per =
    Per(
        accountId = accountId.toAccountId(),
        contractNumber = contractNumber,
        openingDate = openingDate,
        managementTypeId = managementTypeId,
    )

fun Long.toAccountId(): AccountId = AccountId(this)
