package com.example.finvest.modules.account.presentation.mapper

import com.example.finvest.modules.account.domain.cache.ReferenceDataCache
import com.example.finvest.modules.account.domain.models.Account
import com.example.finvest.modules.account.domain.models.AccountStatus
import com.example.finvest.modules.account.domain.models.AccountWithDetails
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
import com.example.finvest.modules.account.domain.valueobject.AccountType
import com.example.finvest.modules.account.domain.valueobject.AccountTypeId
import com.example.finvest.modules.account.domain.valueobject.BankId
import com.example.finvest.modules.account.domain.valueobject.CurrencyCode
import com.example.finvest.modules.account.domain.valueobject.CurrencyId
import com.example.finvest.modules.account.presentation.dto.request.AccountStatusDto
import com.example.finvest.modules.account.presentation.dto.request.AssuranceVieRequest
import com.example.finvest.modules.account.presentation.dto.request.CompteCourantRequest
import com.example.finvest.modules.account.presentation.dto.request.CompteTitreRequest
import com.example.finvest.modules.account.presentation.dto.request.LivretRequest
import com.example.finvest.modules.account.presentation.dto.request.PeaRequest
import com.example.finvest.modules.account.presentation.dto.request.PeeRequest
import com.example.finvest.modules.account.presentation.dto.request.PerRequest
import com.example.finvest.modules.account.presentation.dto.response.AccountDto
import com.example.finvest.modules.account.presentation.dto.response.AccountIdResponse
import com.example.finvest.modules.account.presentation.dto.response.AccountWithDetailsDto
import com.example.finvest.modules.account.presentation.dto.response.AssuranceVieDto
import com.example.finvest.modules.account.presentation.dto.response.BankDto
import com.example.finvest.modules.account.presentation.dto.response.CompteCourantDto
import com.example.finvest.modules.account.presentation.dto.response.CompteTitreDto
import com.example.finvest.modules.account.presentation.dto.response.LivretDto
import com.example.finvest.modules.account.presentation.dto.response.PeaDto
import com.example.finvest.modules.account.presentation.dto.response.PeeDto
import com.example.finvest.modules.account.presentation.dto.response.PerDto

fun Account.toDto(referenceDataCache: ReferenceDataCache): AccountDto {
    return AccountDto(
        accountId = id.value,
        bank = BankDto(
            name = bank.name,
            bic = bank.bic.value,
            logo = bank.logo,
        ),
        name = name,
        balance = balance.amount,
        currency = balance.currencyCode.value,
        createdAt = createdAt,
        closedAt = closedAt,
        accountStatus = accountStatus.toDto(),
        description = description,
        accountType = accountType.value,
    )
}

fun AccountStatus.toDto(): AccountStatusDto {
    return when (this) {
        AccountStatus.ACTIVE -> AccountStatusDto.ACTIVE
        AccountStatus.CLOSED -> AccountStatusDto.CLOSED
        AccountStatus.BLOCKED -> AccountStatusDto.BLOCKED
    }
}

fun AssuranceVieRequest.toAssuranceVieWithDetails(referenceDataCache: ReferenceDataCache) =
    AccountWithDetails(
        account =
            Account(
                id = AccountId(accountId),
                bank = referenceDataCache.bankOf(bankBic),
                name = name,
                balance = Money(balance, CurrencyCode(currencyCode)),
                createdAt = createdAt,
                closedAt = closedAt,
                accountStatus = accountStatus.toDomain(),
                description = description,
                accountType = AccountType(accountType),
            ),
        details =
            AssuranceVie(
                accountId = AccountId(accountId),
                contractNumber = contractNumber,
                openingDate = openingDate,
                managementTypeId = managementTypeId,
            ),
    )

fun AssuranceVie.toDto() = AssuranceVieDto(contractNumber, openingDate, managementTypeId)

@JvmName("toAssuranceVieAccountWithDetailsDto")
fun AccountWithDetails<AssuranceVie>.toDto(referenceDataCache: ReferenceDataCache) = AccountWithDetailsDto(account.toDto(referenceDataCache), details.toDto())

fun CompteCourantRequest.toCompteCourantWithDetails(referenceDataCache: ReferenceDataCache) =
    AccountWithDetails(
        account =
            Account(
                id = AccountId(accountId),
                bank = referenceDataCache.bankOf(bankBic),
                name = name,
                balance = Money(balance, CurrencyCode(currencyCode)),
                createdAt = createdAt,
                closedAt = closedAt,
                accountStatus = accountStatus.toDomain(),
                description = description,
                accountType = AccountType(accountType),
            ),
        details =
            CompteCourant(
                accountId = AccountId(accountId),
                iban = iban,
                bic = bic,
                accountNumber = accountNumber,
                overdraftLimit = overdraftLimit,
                holderName = holderName,
            ),
    )

fun CompteCourant.toDto() = CompteCourantDto(iban, bic, accountNumber, overdraftLimit, holderName)

@JvmName("toCompteCourantAccountWithDetailsDto")
fun AccountWithDetails<CompteCourant>.toDto(referenceDataCache: ReferenceDataCache) = AccountWithDetailsDto(account.toDto(referenceDataCache), details.toDto())

fun CompteTitreRequest.toCompteTitreWithDetails(referenceDataCache: ReferenceDataCache) =
    AccountWithDetails(
        account =
            Account(
                id = AccountId(accountId),
                bank = referenceDataCache.bankOf(bankBic),
                name = name,
                balance = Money(balance, CurrencyCode(currencyCode)),
                createdAt = createdAt,
                closedAt = closedAt,
                accountStatus = accountStatus.toDomain(),
                description = description,
                accountType = AccountType(accountType),
            ),
        details =
            CompteTitre(
                accountId = AccountId(accountId),
                accountNumber = accountNumber,
            ),
    )

fun CompteTitre.toDto() = CompteTitreDto(accountNumber)

@JvmName("toCompteTitreAccountWithDetailsDto")
fun AccountWithDetails<CompteTitre>.toDto(referenceDataCache: ReferenceDataCache) = AccountWithDetailsDto(account.toDto(referenceDataCache), details.toDto())

fun LivretRequest.toLivretWithDetails(referenceDataCache: ReferenceDataCache) =
    AccountWithDetails(
        account =
            Account(
                id = AccountId(accountId),
                bank = referenceDataCache.bankOf(bankBic),
                name = name,
                balance = Money(balance, CurrencyCode(currencyCode)),
                createdAt = createdAt,
                closedAt = closedAt,
                accountStatus = accountStatus.toDomain(),
                description = description,
                accountType = AccountType(accountType),
            ),
        details =
            Livret(
                accountId = AccountId(accountId),
                interestRate = interestRate,
                ceiling = ceiling,
            ),
    )

fun Livret.toDto() = LivretDto(interestRate, ceiling)

@JvmName("toLivretAccountWithDetailsDto")
fun AccountWithDetails<Livret>.toDto(referenceDataCache: ReferenceDataCache) = AccountWithDetailsDto(account.toDto(referenceDataCache), details.toDto())

fun PeaRequest.toPeaWithDetails(referenceDataCache: ReferenceDataCache) =
    AccountWithDetails(
        account =
            Account(
                id = AccountId(accountId),
                bank = referenceDataCache.bankOf(bankBic),
                name = name,
                balance = Money(balance, CurrencyCode(currencyCode)),
                createdAt = createdAt,
                closedAt = closedAt,
                accountStatus = accountStatus.toDomain(),
                description = description,
                accountType = AccountType(accountType),
            ),
        details =
            Pea(
                accountId = AccountId(accountId),
                openingDate = openingDate,
                depositLimit = depositLimit,
            ),
    )

fun Pea.toDto() = PeaDto(openingDate, depositLimit)

@JvmName("toPeaAccountWithDetailsDto")
fun AccountWithDetails<Pea>.toDto(referenceDataCache: ReferenceDataCache) = AccountWithDetailsDto(account.toDto(referenceDataCache), details.toDto())

fun PeeRequest.toPeeWithDetails(referenceDataCache: ReferenceDataCache) =
    AccountWithDetails(
        account =
            Account(
                id = AccountId(accountId),
                bank = referenceDataCache.bankOf(bankBic),
                name = name,
                balance = Money(balance, CurrencyCode(currencyCode)),
                createdAt = createdAt,
                closedAt = closedAt,
                accountStatus = accountStatus.toDomain(),
                description = description,
                accountType = AccountType(accountType),
            ),
        details =
            Pee(
                accountId = AccountId(accountId),
                openingDate = openingDate,
                employer = employer,
            ),
    )

fun Pee.toDto() = PeeDto(openingDate, employer)

@JvmName("toPeeAccountWithDetailsDto")
fun AccountWithDetails<Pee>.toDto(referenceDataCache: ReferenceDataCache) = AccountWithDetailsDto(account.toDto(referenceDataCache), details.toDto())

fun PerRequest.toPerWithDetails(referenceDataCache: ReferenceDataCache) =
    AccountWithDetails(
        account =
            Account(
                id = AccountId(accountId),
                bank = referenceDataCache.bankOf(bankBic),
                name = name,
                balance = Money(balance, CurrencyCode(currencyCode)),
                createdAt = createdAt,
                closedAt = closedAt,
                accountStatus = accountStatus.toDomain(),
                description = description,
                accountType = AccountType(accountType),
            ),
        details =
            Per(
                accountId = AccountId(accountId),
                contractNumber = contractNumber,
                openingDate = openingDate,
                managementTypeId = managementTypeId,
            ),
    )

fun Per.toDto() = PerDto(contractNumber, openingDate, managementTypeId)

@JvmName("toPerAccountWithDetailsDto")
fun AccountWithDetails<Per>.toDto(referenceDataCache: ReferenceDataCache) = AccountWithDetailsDto(account.toDto(referenceDataCache), details.toDto())

fun Long.toAccountIdResponse() = AccountIdResponse(this)

fun Long.toAccountId() = AccountId(this)

fun AccountStatusDto.toDomain() : AccountStatus {
    return when (this) {
        AccountStatusDto.ACTIVE -> AccountStatus.ACTIVE
        AccountStatusDto.CLOSED -> AccountStatus.CLOSED
        AccountStatusDto.BLOCKED -> AccountStatus.BLOCKED
    }
}
