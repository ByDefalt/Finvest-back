package com.example.finvest.modules.account.application.usecase

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.application.usecase.delete.DeletePerUseCase
import com.example.finvest.modules.account.domain.repository.PerRepository
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class DeletePerUseCaseTest {
    private val logger = mock(Logger::class.java)

    @Test
    fun `delegates per deletion`() {
        val repository = mock(PerRepository::class.java)

        DeletePerUseCase(repository, logger)(AccountUseCaseTestFixtures.accountId)

        verify(repository).deletePer(AccountUseCaseTestFixtures.accountId)
    }
}
