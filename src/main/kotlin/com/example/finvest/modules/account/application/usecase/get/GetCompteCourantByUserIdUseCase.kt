package com.example.finvest.modules.account.application.usecase.get

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.domain.models.CompteCourant
import com.example.finvest.modules.account.domain.repository.CompteCourantRepository

class GetCompteCourantByUserIdUseCase(
    private val repository: CompteCourantRepository,
    private val logger: Logger,
) {
    operator fun invoke(userId: Long): List<CompteCourant> {
        logger.info("start invoke")
        val result = repository.getCompteCourantByUserId(userId)
        logger.info("finis invoke")
        return result
    }
}
