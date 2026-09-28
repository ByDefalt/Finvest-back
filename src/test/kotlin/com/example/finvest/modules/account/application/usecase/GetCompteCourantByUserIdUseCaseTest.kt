package com.example.finvest.modules.account.application.usecase

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.domain.repository.CompteCourantRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class GetCompteCourantByUserIdUseCaseTest {
    private val logger = mock(Logger::class.java)

    @Test
    fun `delegates retrieval by user id and returns the result`() {
        val repository = mock(CompteCourantRepository::class.java)
        val expected = listOf(AccountUseCaseTestFixtures.compteCourant)
        `when`(repository.getCompteCourantByUserId(10L)).thenReturn(expected)

        val result = GetCompteCourantByUserIdUseCase(repository, logger)(10L)

        assertThat(result).containsExactlyElementsOf(expected)
        verify(repository).getCompteCourantByUserId(10L)
    }
}
