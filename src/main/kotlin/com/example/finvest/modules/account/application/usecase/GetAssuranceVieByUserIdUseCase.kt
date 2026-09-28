package com.example.finvest.modules.account.application.usecase

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.domain.models.AssuranceVie
import com.example.finvest.modules.account.domain.repository.AssuranceVieRepository

class GetAssuranceVieByUserIdUseCase(
    private val repository: AssuranceVieRepository,
    private val logger: Logger,
) {
    operator fun invoke(userId: Long): List<AssuranceVie> {
        logger.info("start invoke")
        val result = repository.getAssuranceVieByUserId(userId)
        logger.info("finis invoke")
        return result
    }
}
