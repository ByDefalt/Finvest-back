package com.example.finvest.modules.account.application.usecase

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.domain.repository.AssuranceVieRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class GetAssuranceVieByUserIdUseCaseTest {
    private val logger = mock(Logger::class.java)

    @Test
    fun `delegates retrieval by user id and returns the result`() {
        val repository = mock(AssuranceVieRepository::class.java)
        val expected = listOf(AccountUseCaseTestFixtures.assuranceVie)
        `when`(repository.getAssuranceVieByUserId(10L)).thenReturn(expected)

        val result = GetAssuranceVieByUserIdUseCase(repository, logger)(10L)

        assertThat(result).containsExactlyElementsOf(expected)
        verify(repository).getAssuranceVieByUserId(10L)
    }
}
