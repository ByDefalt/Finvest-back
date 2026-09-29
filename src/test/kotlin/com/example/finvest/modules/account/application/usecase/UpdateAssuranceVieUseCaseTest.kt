package com.example.finvest.modules.account.application.usecase

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.application.usecase.update.UpdateAssuranceVieUseCase
import com.example.finvest.modules.account.domain.models.AccountWithDetails
import com.example.finvest.modules.account.domain.repository.AccountRepository
import com.example.finvest.modules.account.domain.repository.AssuranceVieRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class UpdateAssuranceVieUseCaseTest {
    private val logger = mock(Logger::class.java)

    @Test
    fun `delegates assurance vie update and returns the updated value`() {
        val repository = mock(AssuranceVieRepository::class.java)
        val accountRepository = mock(AccountRepository::class.java)
        `when`(accountRepository.updateAccount(AccountUseCaseTestFixtures.account))
            .thenReturn(AccountUseCaseTestFixtures.account)
        `when`(repository.updateAssuranceVie(AccountUseCaseTestFixtures.assuranceVie))
            .thenReturn(AccountUseCaseTestFixtures.assuranceVie)

        val result =
            UpdateAssuranceVieUseCase(
                accountRepository,
                repository,
                AccountUseCaseTestFixtures.transactionManager,
                logger,
            )(
                AccountWithDetails(AccountUseCaseTestFixtures.account, AccountUseCaseTestFixtures.assuranceVie),
            )

        assertThat(result.details).isEqualTo(AccountUseCaseTestFixtures.assuranceVie)
        verify(repository).updateAssuranceVie(AccountUseCaseTestFixtures.assuranceVie)
    }
}
