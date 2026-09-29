package com.example.finvest.modules.account.presentation.mapper

import com.example.finvest.modules.account.domain.models.Account
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
import com.example.finvest.modules.account.domain.valueobject.AccountTypeId
import com.example.finvest.modules.account.domain.valueobject.BankId
import com.example.finvest.modules.account.domain.valueobject.CurrencyId
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
import com.example.finvest.modules.account.presentation.dto.response.CompteCourantDto
import com.example.finvest.modules.account.presentation.dto.response.CompteTitreDto
import com.example.finvest.modules.account.presentation.dto.response.LivretDto
import com.example.finvest.modules.account.presentation.dto.response.PeaDto
import com.example.finvest.modules.account.presentation.dto.response.PeeDto
import com.example.finvest.modules.account.presentation.dto.response.PerDto

fun Account.toDto() =
    AccountDto(
        accountId = id.value,
        bankId = bankId.value,
        name = name,
        balance = balance.amount,
        currencyId = balance.currencyId.value,
        createdAt = createdAt,
        closedAt = closedAt,
        accountStatusId = accountStatusId.value,
        description = description,
        accountTypeId = accountTypeId.value,
    )

fun AssuranceVieRequest.toAssuranceVieWithDetails() =
    AccountWithDetails(
        account =
            Account(
                id = AccountId(accountId),
                bankId = BankId(bankId),
                name = name,
                balance = Money(balance, CurrencyId(currencyId)),
                createdAt = createdAt,
                closedAt = closedAt,
                accountStatusId = AccountStatusId(accountStatusId),
                description = description,
                accountTypeId = AccountTypeId(accountTypeId),
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
fun AccountWithDetails<AssuranceVie>.toDto() =
    AccountWithDetailsDto(account.toDto(), details.toDto())

fun CompteCourantRequest.toCompteCourantWithDetails() =
    AccountWithDetails(
        account =
            Account(
                id = AccountId(accountId),
                bankId = BankId(bankId),
                name = name,
                balance = Money(balance, CurrencyId(currencyId)),
                createdAt = createdAt,
                closedAt = closedAt,
                accountStatusId = AccountStatusId(accountStatusId),
                description = description,
                accountTypeId = AccountTypeId(accountTypeId),
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
fun AccountWithDetails<CompteCourant>.toDto() =
    AccountWithDetailsDto(account.toDto(), details.toDto())

fun CompteTitreRequest.toCompteTitreWithDetails() =
    AccountWithDetails(
        account =
            Account(
                id = AccountId(accountId),
                bankId = BankId(bankId),
                name = name,
                balance = Money(balance, CurrencyId(currencyId)),
                createdAt = createdAt,
                closedAt = closedAt,
                accountStatusId = AccountStatusId(accountStatusId),
                description = description,
                accountTypeId = AccountTypeId(accountTypeId),
            ),
        details =
            CompteTitre(
                accountId = AccountId(accountId),
                accountNumber = accountNumber,
            ),
    )

fun CompteTitre.toDto() = CompteTitreDto(accountNumber)

@JvmName("toCompteTitreAccountWithDetailsDto")
fun AccountWithDetails<CompteTitre>.toDto() =
    AccountWithDetailsDto(account.toDto(), details.toDto())

fun LivretRequest.toLivretWithDetails() =
    AccountWithDetails(
        account =
            Account(
                id = AccountId(accountId),
                bankId = BankId(bankId),
                name = name,
                balance = Money(balance, CurrencyId(currencyId)),
                createdAt = createdAt,
                closedAt = closedAt,
                accountStatusId = AccountStatusId(accountStatusId),
                description = description,
                accountTypeId = AccountTypeId(accountTypeId),
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
fun AccountWithDetails<Livret>.toDto() =
    AccountWithDetailsDto(account.toDto(), details.toDto())

fun PeaRequest.toPeaWithDetails() =
    AccountWithDetails(
        account =
            Account(
                id = AccountId(accountId),
                bankId = BankId(bankId),
                name = name,
                balance = Money(balance, CurrencyId(currencyId)),
                createdAt = createdAt,
                closedAt = closedAt,
                accountStatusId = AccountStatusId(accountStatusId),
                description = description,
                accountTypeId = AccountTypeId(accountTypeId),
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
fun AccountWithDetails<Pea>.toDto() =
    AccountWithDetailsDto(account.toDto(), details.toDto())

fun PeeRequest.toPeeWithDetails() =
    AccountWithDetails(
        account =
            Account(
                id = AccountId(accountId),
                bankId = BankId(bankId),
                name = name,
                balance = Money(balance, CurrencyId(currencyId)),
                createdAt = createdAt,
                closedAt = closedAt,
                accountStatusId = AccountStatusId(accountStatusId),
                description = description,
                accountTypeId = AccountTypeId(accountTypeId),
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
fun AccountWithDetails<Pee>.toDto() =
    AccountWithDetailsDto(account.toDto(), details.toDto())

fun PerRequest.toPerWithDetails() =
    AccountWithDetails(
        account =
            Account(
                id = AccountId(accountId),
                bankId = BankId(bankId),
                name = name,
                balance = Money(balance, CurrencyId(currencyId)),
                createdAt = createdAt,
                closedAt = closedAt,
                accountStatusId = AccountStatusId(accountStatusId),
                description = description,
                accountTypeId = AccountTypeId(accountTypeId),
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
fun AccountWithDetails<Per>.toDto() =
    AccountWithDetailsDto(account.toDto(), details.toDto())

fun Long.toAccountIdResponse() = AccountIdResponse(this)

fun Long.toAccountId() = AccountId(this)
