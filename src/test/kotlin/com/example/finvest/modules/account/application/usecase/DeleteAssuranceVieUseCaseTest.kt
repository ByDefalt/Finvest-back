package com.example.finvest.modules.account.application.usecase

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.application.usecase.delete.DeleteAssuranceVieUseCase
import com.example.finvest.modules.account.domain.repository.AssuranceVieRepository
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class DeleteAssuranceVieUseCaseTest {
    private val logger = mock(Logger::class.java)

    @Test
    fun `delegates assurance vie deletion`() {
        val repository = mock(AssuranceVieRepository::class.java)

        DeleteAssuranceVieUseCase(repository, logger)(AccountUseCaseTestFixtures.accountId)

        verify(repository).deleteAssuranceVie(AccountUseCaseTestFixtures.accountId)
    }
}
