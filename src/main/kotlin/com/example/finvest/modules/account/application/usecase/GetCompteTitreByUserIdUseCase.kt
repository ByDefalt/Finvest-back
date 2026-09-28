package com.example.finvest.modules.account.application.usecase

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.domain.models.CompteTitre
import com.example.finvest.modules.account.domain.repository.CompteTitreRepository

class GetCompteTitreByUserIdUseCase(
    private val repository: CompteTitreRepository,
    private val logger: Logger,
) {
    operator fun invoke(userId: Long): List<CompteTitre> {
        logger.info("start invoke")
        val result = repository.getCompteTitreByUserId(userId)
        logger.info("finis invoke")
        return result
    }
}
