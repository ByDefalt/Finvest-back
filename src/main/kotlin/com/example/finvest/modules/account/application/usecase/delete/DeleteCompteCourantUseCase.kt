package com.example.finvest.modules.account.application.usecase.delete

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.domain.repository.CompteCourantRepository
import com.example.finvest.modules.account.domain.valueobject.AccountId

class DeleteCompteCourantUseCase(
    private val repository: CompteCourantRepository,
    private val logger: Logger,
) {
    operator fun invoke(accountId: AccountId) {
        logger.info("start invoke")
        repository.deleteCompteCourant(accountId)
        logger.info("finis invoke")
    }
}
