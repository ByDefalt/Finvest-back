package com.example.finvest.modules.account.application.usecase

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.domain.models.AccountWithDetails
import com.example.finvest.modules.account.domain.repository.AccountRepository
import com.example.finvest.modules.account.domain.repository.LivretRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class UpdateLivretUseCaseTest {
    private val logger = mock(Logger::class.java)

    @Test
    fun `delegates livret update and returns the updated value`() {
        val repository = mock(LivretRepository::class.java)
        val accountRepository = mock(AccountRepository::class.java)
        `when`(accountRepository.updateAccount(AccountUseCaseTestFixtures.account))
            .thenReturn(AccountUseCaseTestFixtures.account)
        `when`(repository.updateLivret(AccountUseCaseTestFixtures.livret))
            .thenReturn(AccountUseCaseTestFixtures.livret)

        val result =
            UpdateLivretUseCase(accountRepository, repository, AccountUseCaseTestFixtures.transactionManager, logger)(
                AccountWithDetails(AccountUseCaseTestFixtures.account, AccountUseCaseTestFixtures.livret),
            )

        assertThat(result.details).isEqualTo(AccountUseCaseTestFixtures.livret)
        verify(repository).updateLivret(AccountUseCaseTestFixtures.livret)
    }
}
