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

class UpdatePeeUseCaseTest {
    private val logger = mock(Logger::class.java)

    @Test
    fun `delegates pee update and returns the updated value`() {
        val repository = mock(PeeRepository::class.java)
        val accountRepository = mock(AccountRepository::class.java)
        `when`(accountRepository.updateAccount(AccountUseCaseTestFixtures.account))
            .thenReturn(AccountUseCaseTestFixtures.account)
        `when`(repository.updatePee(AccountUseCaseTestFixtures.pee))
            .thenReturn(AccountUseCaseTestFixtures.pee)

        val result =
            UpdatePeeUseCase(accountRepository, repository, AccountUseCaseTestFixtures.transactionManager, logger)(
                AccountWithDetails(AccountUseCaseTestFixtures.account, AccountUseCaseTestFixtures.pee),
            )

        assertThat(result.details).isEqualTo(AccountUseCaseTestFixtures.pee)
        verify(repository).updatePee(AccountUseCaseTestFixtures.pee)
    }
}
