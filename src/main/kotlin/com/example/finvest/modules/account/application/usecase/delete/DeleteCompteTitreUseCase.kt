package com.example.finvest.modules.account.application.usecase.delete

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.domain.repository.CompteTitreRepository
import com.example.finvest.modules.account.domain.valueobject.AccountId

class DeleteCompteTitreUseCase(
    private val repository: CompteTitreRepository,
    private val logger: Logger,
) {
    operator fun invoke(accountId: AccountId) {
        logger.info("start invoke")
        repository.deleteCompteTitre(accountId)
        logger.info("finis invoke")
    }
}
