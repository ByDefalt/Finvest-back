package com.example.finvest.modules.account.application.usecase.get

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.domain.models.Per
import com.example.finvest.modules.account.domain.repository.PerRepository

class GetPerByUserIdUseCase(
    private val repository: PerRepository,
    private val logger: Logger,
) {
    operator fun invoke(userId: Long): List<Per> {
        logger.info("start invoke")
        val result = repository.getPerByUserId(userId)
        logger.info("finis invoke")
        return result
    }
}
