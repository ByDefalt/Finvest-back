package com.example.finvest.modules.account.application.usecase

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.domain.repository.CompteTitreRepository
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class DeleteCompteTitreUseCaseTest {
    private val logger = mock(Logger::class.java)

    @Test
    fun `delegates compte titre deletion`() {
        val repository = mock(CompteTitreRepository::class.java)

        DeleteCompteTitreUseCase(repository, logger)(AccountUseCaseTestFixtures.accountId)

        verify(repository).deleteCompteTitre(AccountUseCaseTestFixtures.accountId)
    }
}
