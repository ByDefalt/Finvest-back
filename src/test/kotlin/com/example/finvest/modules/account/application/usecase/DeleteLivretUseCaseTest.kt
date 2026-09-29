package com.example.finvest.modules.account.application.usecase

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.application.usecase.delete.DeleteLivretUseCase
import com.example.finvest.modules.account.domain.repository.LivretRepository
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class DeleteLivretUseCaseTest {
    private val logger = mock(Logger::class.java)

    @Test
    fun `delegates livret deletion`() {
        val repository = mock(LivretRepository::class.java)

        DeleteLivretUseCase(repository, logger)(AccountUseCaseTestFixtures.accountId)

        verify(repository).deleteLivret(AccountUseCaseTestFixtures.accountId)
    }
}
