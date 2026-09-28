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

class CreatePeaUseCaseTest {
    private val logger = mock(Logger::class.java)

    @Test
    fun `delegates pea creation and returns the created id`() {
        val repository = mock(PeaRepository::class.java)
        val accountRepository = mock(AccountRepository::class.java)
        `when`(accountRepository.createAccount(AccountUseCaseTestFixtures.account))
            .thenReturn(AccountUseCaseTestFixtures.accountId)
        `when`(repository.createPea(AccountUseCaseTestFixtures.pea))
            .thenReturn(AccountUseCaseTestFixtures.accountId)

        val result =
            CreatePeaUseCase(accountRepository, repository, AccountUseCaseTestFixtures.transactionManager, logger)(
                AccountWithDetails(AccountUseCaseTestFixtures.account, AccountUseCaseTestFixtures.pea),
            )

        assertThat(result).isEqualTo(AccountUseCaseTestFixtures.accountId)
        verify(repository).createPea(AccountUseCaseTestFixtures.pea)
    }
}
