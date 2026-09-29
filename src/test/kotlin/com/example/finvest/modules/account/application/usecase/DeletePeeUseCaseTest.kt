package com.example.finvest.modules.account.application.usecase

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.application.usecase.delete.DeletePeeUseCase
import com.example.finvest.modules.account.domain.repository.PeeRepository
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class DeletePeeUseCaseTest {
    private val logger = mock(Logger::class.java)

    @Test
    fun `delegates pee deletion`() {
        val repository = mock(PeeRepository::class.java)

        DeletePeeUseCase(repository, logger)(AccountUseCaseTestFixtures.accountId)

        verify(repository).deletePee(AccountUseCaseTestFixtures.accountId)
    }
}
