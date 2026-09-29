package com.example.finvest.modules.account.application.usecase

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.application.usecase.create.CreateAssuranceVieUseCase
import com.example.finvest.modules.account.domain.models.AccountWithDetails
import com.example.finvest.modules.account.domain.repository.AccountRepository
import com.example.finvest.modules.account.domain.repository.AssuranceVieRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class CreateAssuranceVieUseCaseTest {
    private val logger = mock(Logger::class.java)

    @Test
    fun `delegates assurance vie creation and returns the created id`() {
        val repository = mock(AssuranceVieRepository::class.java)
        val accountRepository = mock(AccountRepository::class.java)
        `when`(accountRepository.createAccount(AccountUseCaseTestFixtures.account))
            .thenReturn(AccountUseCaseTestFixtures.accountId)
        `when`(repository.createAssuranceVie(AccountUseCaseTestFixtures.assuranceVie))
            .thenReturn(AccountUseCaseTestFixtures.accountId)

        val result =
            CreateAssuranceVieUseCase(
                accountRepository,
                repository,
                AccountUseCaseTestFixtures.transactionManager,
                logger,
            )(
                AccountWithDetails(AccountUseCaseTestFixtures.account, AccountUseCaseTestFixtures.assuranceVie),
            )

        assertThat(result).isEqualTo(AccountUseCaseTestFixtures.accountId)
        verify(repository).createAssuranceVie(AccountUseCaseTestFixtures.assuranceVie)
    }
}
