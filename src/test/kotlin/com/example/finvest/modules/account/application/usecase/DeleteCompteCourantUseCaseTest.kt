package com.example.finvest.modules.account.application.usecase

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.application.usecase.delete.DeleteCompteCourantUseCase
import com.example.finvest.modules.account.domain.repository.CompteCourantRepository
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class DeleteCompteCourantUseCaseTest {
    private val logger = mock(Logger::class.java)

    @Test
    fun `delegates compte courant deletion`() {
        val repository = mock(CompteCourantRepository::class.java)

        DeleteCompteCourantUseCase(repository, logger)(AccountUseCaseTestFixtures.accountId)

        verify(repository).deleteCompteCourant(AccountUseCaseTestFixtures.accountId)
    }
}
