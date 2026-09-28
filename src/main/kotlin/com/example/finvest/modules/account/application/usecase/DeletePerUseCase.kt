package com.example.finvest.modules.account.application.usecase

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.domain.repository.PerRepository
import com.example.finvest.modules.account.domain.valueobject.AccountId

class DeletePerUseCase(
    private val repository: PerRepository,
    private val logger: Logger,
) {
    operator fun invoke(accountId: AccountId) {
        logger.info("start invoke")
        repository.deletePer(accountId)
        logger.info("finis invoke")
    }
}
