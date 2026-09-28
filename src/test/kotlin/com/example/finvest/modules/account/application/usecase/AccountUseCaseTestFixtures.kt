package com.example.finvest.modules.account.application.usecase

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
import com.example.finvest.modules.shared.application.manager.TransactionManager
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

internal object AccountUseCaseTestFixtures {
    val transactionManager =
        object : TransactionManager {
            override fun <T> execute(block: () -> T): T = block()
        }

    val accountId = AccountId(1L)

    val account =
        Account(
            id = accountId,
            bankId = BankId(2L),
            name = "Account",
            balance = Money(BigDecimal("100.00"), CurrencyId(3L)),
            createdAt = LocalDateTime.of(2024, 1, 1, 0, 0),
            closedAt = null,
            accountStatusId = AccountStatusId(4L),
            description = "Description",
            accountTypeId = AccountTypeId(5L),
        )

    val assuranceVie =
        AssuranceVie(
            accountId,
            "AV-1",
            LocalDate.of(2024, 1, 1),
            6L,
        )

    val compteCourant =
        CompteCourant(
            accountId,
            "FR761234",
            "BIC123",
            "123456",
            BigDecimal("100.00"),
            "Owner",
        )

    val compteTitre = CompteTitre(accountId, "CT-1")
    val livret = Livret(accountId, BigDecimal("2.00"), BigDecimal("1000.00"))
    val pea = Pea(accountId, LocalDate.of(2024, 1, 1), BigDecimal("150000.00"))
    val pee = Pee(accountId, LocalDate.of(2024, 1, 1), "Employer")
    val per = Per(accountId, "PER-1", LocalDate.of(2024, 1, 1), 6L)
}
