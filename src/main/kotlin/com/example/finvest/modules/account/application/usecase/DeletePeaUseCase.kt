package com.example.finvest.modules.account.application.usecase

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.domain.repository.PeaRepository
import com.example.finvest.modules.account.domain.valueobject.AccountId

class DeletePeaUseCase(
    private val repository: PeaRepository,
    private val logger: Logger,
) {
    operator fun invoke(accountId: AccountId) {
        logger.info("start invoke")
        repository.deletePea(accountId)
        logger.info("finis invoke")
    }
}
