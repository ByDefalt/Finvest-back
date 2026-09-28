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

class UpdateCompteCourantUseCaseTest {
    private val logger = mock(Logger::class.java)

    @Test
    fun `delegates compte courant update and returns the updated value`() {
        val repository = mock(CompteCourantRepository::class.java)
        val accountRepository = mock(AccountRepository::class.java)
        `when`(accountRepository.updateAccount(AccountUseCaseTestFixtures.account))
            .thenReturn(AccountUseCaseTestFixtures.account)
        `when`(repository.updateCompteCourant(AccountUseCaseTestFixtures.compteCourant))
            .thenReturn(AccountUseCaseTestFixtures.compteCourant)

        val result =
            UpdateCompteCourantUseCase(
                accountRepository,
                repository,
                AccountUseCaseTestFixtures.transactionManager,
                logger,
            )(
                AccountWithDetails(AccountUseCaseTestFixtures.account, AccountUseCaseTestFixtures.compteCourant),
            )

        assertThat(result.details).isEqualTo(AccountUseCaseTestFixtures.compteCourant)
        verify(repository).updateCompteCourant(AccountUseCaseTestFixtures.compteCourant)
    }
}
