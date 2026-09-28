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
import com.example.finvest.modules.account.presentation.dto.AccountIdResponse
import com.example.finvest.modules.account.presentation.dto.AccountRequest
import com.example.finvest.modules.account.presentation.dto.AccountResponse
import com.example.finvest.modules.account.presentation.dto.AssuranceVieDto
import com.example.finvest.modules.account.presentation.dto.AssuranceVieRequest
import com.example.finvest.modules.account.presentation.dto.CompteCourantDto
import com.example.finvest.modules.account.presentation.dto.CompteCourantRequest
import com.example.finvest.modules.account.presentation.dto.CompteTitreDto
import com.example.finvest.modules.account.presentation.dto.CompteTitreRequest
import com.example.finvest.modules.account.presentation.dto.LivretDto
import com.example.finvest.modules.account.presentation.dto.LivretRequest
import com.example.finvest.modules.account.presentation.dto.PeaDto
import com.example.finvest.modules.account.presentation.dto.PeaRequest
import com.example.finvest.modules.account.presentation.dto.PeeDto
import com.example.finvest.modules.account.presentation.dto.PeeRequest
import com.example.finvest.modules.account.presentation.dto.PerDto
import com.example.finvest.modules.account.presentation.dto.PerRequest

fun Account.toResponse() =
    AccountResponse(
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

fun AccountRequest.toAccountDomain() =
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

fun AccountWithDetails<AssuranceVie>.toDto() =
    AssuranceVieDto(
        contractNumber = details.contractNumber,
        openingDate = details.openingDate,
        managementTypeId = details.managementTypeId,
        accountId = account.id.value,
        bankId = account.bankId.value,
        name = account.name,
        balance = account.balance.amount,
        currencyId = account.balance.currencyId.value,
        createdAt = account.createdAt,
        closedAt = account.closedAt,
        accountStatusId = account.accountStatusId.value,
        description = account.description,
        accountTypeId = account.accountTypeId.value,
    )

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

fun AccountWithDetails<CompteCourant>.toDto() =
    CompteCourantDto(
        iban = details.iban,
        bic = details.bic,
        accountNumber = details.accountNumber,
        overdraftLimit = details.overdraftLimit,
        holderName = details.holderName,
        accountId = account.id.value,
        bankId = account.bankId.value,
        name = account.name,
        balance = account.balance.amount,
        currencyId = account.balance.currencyId.value,
        createdAt = account.createdAt,
        closedAt = account.closedAt,
        accountStatusId = account.accountStatusId.value,
        description = account.description,
        accountTypeId = account.accountTypeId.value,
    )

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

fun AccountWithDetails<CompteTitre>.toDto() =
    CompteTitreDto(
        accountNumber = details.accountNumber,
        accountId = account.id.value,
        bankId = account.bankId.value,
        name = account.name,
        balance = account.balance.amount,
        currencyId = account.balance.currencyId.value,
        createdAt = account.createdAt,
        closedAt = account.closedAt,
        accountStatusId = account.accountStatusId.value,
        description = account.description,
        accountTypeId = account.accountTypeId.value,
    )

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

fun AccountWithDetails<Livret>.toDto() =
    LivretDto(
        interestRate = details.interestRate,
        ceiling = details.ceiling,
        accountId = account.id.value,
        bankId = account.bankId.value,
        name = account.name,
        balance = account.balance.amount,
        currencyId = account.balance.currencyId.value,
        createdAt = account.createdAt,
        closedAt = account.closedAt,
        accountStatusId = account.accountStatusId.value,
        description = account.description,
        accountTypeId = account.accountTypeId.value,
    )

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

fun AccountWithDetails<Pea>.toDto() =
    PeaDto(
        openingDate = details.openingDate,
        depositLimit = details.depositLimit,
        accountId = account.id.value,
        bankId = account.bankId.value,
        name = account.name,
        balance = account.balance.amount,
        currencyId = account.balance.currencyId.value,
        createdAt = account.createdAt,
        closedAt = account.closedAt,
        accountStatusId = account.accountStatusId.value,
        description = account.description,
        accountTypeId = account.accountTypeId.value,
    )

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

fun AccountWithDetails<Pee>.toDto() =
    PeeDto(
        openingDate = details.openingDate,
        employer = details.employer,
        accountId = account.id.value,
        bankId = account.bankId.value,
        name = account.name,
        balance = account.balance.amount,
        currencyId = account.balance.currencyId.value,
        createdAt = account.createdAt,
        closedAt = account.closedAt,
        accountStatusId = account.accountStatusId.value,
        description = account.description,
        accountTypeId = account.accountTypeId.value,
    )

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

fun AccountWithDetails<Per>.toDto() =
    PerDto(
        contractNumber = details.contractNumber,
        openingDate = details.openingDate,
        managementTypeId = details.managementTypeId,
        accountId = account.id.value,
        bankId = account.bankId.value,
        name = account.name,
        balance = account.balance.amount,
        currencyId = account.balance.currencyId.value,
        createdAt = account.createdAt,
        closedAt = account.closedAt,
        accountStatusId = account.accountStatusId.value,
        description = account.description,
        accountTypeId = account.accountTypeId.value,
    )

fun Long.toAccountIdResponse() = AccountIdResponse(this)

fun Long.toAccountId() = AccountId(this)
