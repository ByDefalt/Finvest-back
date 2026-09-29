package com.example.finvest.modules.account.application.usecase.get

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.domain.models.Pee
import com.example.finvest.modules.account.domain.repository.PeeRepository

class GetPeeByUserIdUseCase(
    private val repository: PeeRepository,
    private val logger: Logger,
) {
    operator fun invoke(userId: Long): List<Pee> {
        logger.info("start invoke")
        val result = repository.getPeeByUserId(userId)
        logger.info("finis invoke")
        return result
    }
}
