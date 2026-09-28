package com.example.finvest.modules.account.application.usecase

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.domain.models.AccountWithDetails
import com.example.finvest.modules.account.domain.repository.AccountRepository
import com.example.finvest.modules.account.domain.repository.PeeRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class CreatePeeUseCaseTest {
    private val logger = mock(Logger::class.java)

    @Test
    fun `delegates pee creation and returns the created id`() {
        val repository = mock(PeeRepository::class.java)
        val accountRepository = mock(AccountRepository::class.java)
        `when`(accountRepository.createAccount(AccountUseCaseTestFixtures.account))
            .thenReturn(AccountUseCaseTestFixtures.accountId)
        `when`(repository.createPee(AccountUseCaseTestFixtures.pee))
            .thenReturn(AccountUseCaseTestFixtures.accountId)

        val result =
            CreatePeeUseCase(accountRepository, repository, AccountUseCaseTestFixtures.transactionManager, logger)(
                AccountWithDetails(AccountUseCaseTestFixtures.account, AccountUseCaseTestFixtures.pee),
            )

        assertThat(result).isEqualTo(AccountUseCaseTestFixtures.accountId)
        verify(repository).createPee(AccountUseCaseTestFixtures.pee)
    }
}
