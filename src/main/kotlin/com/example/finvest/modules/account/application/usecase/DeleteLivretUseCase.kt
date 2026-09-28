package com.example.finvest.modules.account.application.usecase

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.domain.repository.LivretRepository
import com.example.finvest.modules.account.domain.valueobject.AccountId

class DeleteLivretUseCase(
    private val repository: LivretRepository,
    private val logger: Logger,
) {
    operator fun invoke(accountId: AccountId) {
        logger.info("start invoke")
        repository.deleteLivret(accountId)
        logger.info("finis invoke")
    }
}
