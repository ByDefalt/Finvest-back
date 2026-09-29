package com.example.finvest.modules.account.application.usecase.get

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.domain.models.Pea
import com.example.finvest.modules.account.domain.repository.PeaRepository

class GetPeaByUserIdUseCase(
    private val repository: PeaRepository,
    private val logger: Logger,
) {
    operator fun invoke(userId: Long): List<Pea> {
        logger.info("start invoke")
        val result = repository.getPeaByUserId(userId)
        logger.info("finis invoke")
        return result
    }
}
