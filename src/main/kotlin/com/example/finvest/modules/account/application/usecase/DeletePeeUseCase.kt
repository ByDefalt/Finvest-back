package com.example.finvest.modules.account.application.usecase

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.domain.repository.PeeRepository
import com.example.finvest.modules.account.domain.valueobject.AccountId

class DeletePeeUseCase(
    private val repository: PeeRepository,
    private val logger: Logger,
) {
    operator fun invoke(accountId: AccountId) {
        logger.info("start invoke")
        repository.deletePee(accountId)
        logger.info("finis invoke")
    }
}
