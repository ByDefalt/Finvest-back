package com.example.finvest.modules.account.application.usecase

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.domain.models.AccountWithDetails
import com.example.finvest.modules.account.domain.repository.AccountRepository
import com.example.finvest.modules.account.domain.repository.PerRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class UpdatePerUseCaseTest {
    private val logger = mock(Logger::class.java)

    @Test
    fun `delegates per update and returns the updated value`() {
        val repository = mock(PerRepository::class.java)
        val accountRepository = mock(AccountRepository::class.java)
        `when`(accountRepository.updateAccount(AccountUseCaseTestFixtures.account))
            .thenReturn(AccountUseCaseTestFixtures.account)
        `when`(repository.updatePer(AccountUseCaseTestFixtures.per))
            .thenReturn(AccountUseCaseTestFixtures.per)

        val result =
            UpdatePerUseCase(accountRepository, repository, AccountUseCaseTestFixtures.transactionManager, logger)(
                AccountWithDetails(AccountUseCaseTestFixtures.account, AccountUseCaseTestFixtures.per),
            )

        assertThat(result.details).isEqualTo(AccountUseCaseTestFixtures.per)
        verify(repository).updatePer(AccountUseCaseTestFixtures.per)
    }
}
