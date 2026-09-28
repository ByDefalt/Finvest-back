package com.example.finvest.modules.account.application.usecase

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.domain.models.AccountWithDetails
import com.example.finvest.modules.account.domain.repository.AccountRepository
import com.example.finvest.modules.account.domain.repository.PeaRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class UpdatePeaUseCaseTest {
    private val logger = mock(Logger::class.java)

    @Test
    fun `delegates pea update and returns the updated value`() {
        val repository = mock(PeaRepository::class.java)
        val accountRepository = mock(AccountRepository::class.java)
        `when`(accountRepository.updateAccount(AccountUseCaseTestFixtures.account))
            .thenReturn(AccountUseCaseTestFixtures.account)
        `when`(repository.updatePea(AccountUseCaseTestFixtures.pea))
            .thenReturn(AccountUseCaseTestFixtures.pea)

        val result =
            UpdatePeaUseCase(accountRepository, repository, AccountUseCaseTestFixtures.transactionManager, logger)(
                AccountWithDetails(AccountUseCaseTestFixtures.account, AccountUseCaseTestFixtures.pea),
            )

        assertThat(result.details).isEqualTo(AccountUseCaseTestFixtures.pea)
        verify(repository).updatePea(AccountUseCaseTestFixtures.pea)
    }
}
