package com.example.finvest.modules.account.application.usecase.create

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.domain.models.AccountWithDetails
import com.example.finvest.modules.account.domain.models.Pea
import com.example.finvest.modules.account.domain.repository.AccountRepository
import com.example.finvest.modules.account.domain.repository.PeaRepository
import com.example.finvest.modules.account.domain.valueobject.AccountId
import com.example.finvest.modules.shared.application.manager.TransactionManager

class CreatePeaUseCase(
    private val accountRepository: AccountRepository,
    private val repository: PeaRepository,
    private val transactionManager: TransactionManager,
    private val logger: Logger,
) {
    operator fun invoke(accountWithDetails: AccountWithDetails<Pea>): AccountId {
        logger.info("start invoke")
        val result =
            transactionManager.execute {
                val accountId = accountRepository.createAccount(accountWithDetails.account)
                return@execute repository.createPea(accountWithDetails.details.copy(accountId = accountId))
            }
        logger.info("finis invoke")
        return result
    }
}
