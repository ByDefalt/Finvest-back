package com.example.finvest.modules.account.application.usecase

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.application.usecase.update.UpdateCompteTitreUseCase
import com.example.finvest.modules.account.domain.models.AccountWithDetails
import com.example.finvest.modules.account.domain.repository.AccountRepository
import com.example.finvest.modules.account.domain.repository.CompteTitreRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class UpdateCompteTitreUseCaseTest {
    private val logger = mock(Logger::class.java)

    @Test
    fun `delegates compte titre update and returns the updated value`() {
        val repository = mock(CompteTitreRepository::class.java)
        val accountRepository = mock(AccountRepository::class.java)
        `when`(accountRepository.updateAccount(AccountUseCaseTestFixtures.account))
            .thenReturn(AccountUseCaseTestFixtures.account)
        `when`(repository.updateCompteTitre(AccountUseCaseTestFixtures.compteTitre))
            .thenReturn(AccountUseCaseTestFixtures.compteTitre)

        val result =
            UpdateCompteTitreUseCase(
                accountRepository,
                repository,
                AccountUseCaseTestFixtures.transactionManager,
                logger,
            )(
                AccountWithDetails(AccountUseCaseTestFixtures.account, AccountUseCaseTestFixtures.compteTitre),
            )

        assertThat(result.details).isEqualTo(AccountUseCaseTestFixtures.compteTitre)
        verify(repository).updateCompteTitre(AccountUseCaseTestFixtures.compteTitre)
    }
}
