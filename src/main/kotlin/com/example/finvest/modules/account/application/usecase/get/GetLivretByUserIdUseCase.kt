package com.example.finvest.modules.account.application.usecase.get

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.domain.models.Livret
import com.example.finvest.modules.account.domain.repository.LivretRepository

class GetLivretByUserIdUseCase(
    private val repository: LivretRepository,
    private val logger: Logger,
) {
    operator fun invoke(userId: Long): List<Livret> {
        logger.info("start invoke")
        val result = repository.getLivretByUserId(userId)
        logger.info("finis invoke")
        return result
    }
}
