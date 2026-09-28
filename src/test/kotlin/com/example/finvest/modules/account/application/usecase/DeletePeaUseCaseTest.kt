package com.example.finvest.modules.account.application.usecase

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.domain.repository.PeaRepository
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class DeletePeaUseCaseTest {
    private val logger = mock(Logger::class.java)

    @Test
    fun `delegates pea deletion`() {
        val repository = mock(PeaRepository::class.java)

        DeletePeaUseCase(repository, logger)(AccountUseCaseTestFixtures.accountId)

        verify(repository).deletePea(AccountUseCaseTestFixtures.accountId)
    }
}
