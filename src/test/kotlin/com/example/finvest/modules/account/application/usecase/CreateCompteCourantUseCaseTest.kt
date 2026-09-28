package com.example.finvest.modules.account.application.usecase

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.domain.models.AccountWithDetails
import com.example.finvest.modules.account.domain.repository.AccountRepository
import com.example.finvest.modules.account.domain.repository.CompteCourantRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class CreateCompteCourantUseCaseTest {
    private val logger = mock(Logger::class.java)

    @Test
    fun `delegates compte courant creation and returns the created id`() {
        val repository = mock(CompteCourantRepository::class.java)
        val accountRepository = mock(AccountRepository::class.java)
        `when`(accountRepository.createAccount(AccountUseCaseTestFixtures.account))
            .thenReturn(AccountUseCaseTestFixtures.accountId)
        `when`(repository.createCompteCourant(AccountUseCaseTestFixtures.compteCourant))
            .thenReturn(AccountUseCaseTestFixtures.accountId)

        val result =
            CreateCompteCourantUseCase(
                accountRepository,
                repository,
                AccountUseCaseTestFixtures.transactionManager,
                logger,
            )(
                AccountWithDetails(AccountUseCaseTestFixtures.account, AccountUseCaseTestFixtures.compteCourant),
            )

        assertThat(result).isEqualTo(AccountUseCaseTestFixtures.accountId)
        verify(repository).createCompteCourant(AccountUseCaseTestFixtures.compteCourant)
    }
}
