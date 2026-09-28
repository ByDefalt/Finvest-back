package com.example.finvest.modules.account.application.usecase

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.domain.models.AccountWithDetails
import com.example.finvest.modules.account.domain.repository.AccountRepository
import com.example.finvest.modules.account.domain.repository.CompteTitreRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class CreateCompteTitreUseCaseTest {
    private val logger = mock(Logger::class.java)

    @Test
    fun `delegates compte titre creation and returns the created id`() {
        val repository = mock(CompteTitreRepository::class.java)
        val accountRepository = mock(AccountRepository::class.java)
        `when`(accountRepository.createAccount(AccountUseCaseTestFixtures.account))
            .thenReturn(AccountUseCaseTestFixtures.accountId)
        `when`(repository.createCompteTitre(AccountUseCaseTestFixtures.compteTitre))
            .thenReturn(AccountUseCaseTestFixtures.accountId)

        val result =
            CreateCompteTitreUseCase(
                accountRepository,
                repository,
                AccountUseCaseTestFixtures.transactionManager,
                logger,
            )(
                AccountWithDetails(AccountUseCaseTestFixtures.account, AccountUseCaseTestFixtures.compteTitre),
            )

        assertThat(result).isEqualTo(AccountUseCaseTestFixtures.accountId)
        verify(repository).createCompteTitre(AccountUseCaseTestFixtures.compteTitre)
    }
}
