package com.example.finvest.modules.account.application.usecase

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.domain.models.AccountWithDetails
import com.example.finvest.modules.account.domain.models.Pea
import com.example.finvest.modules.account.domain.repository.AccountRepository
import com.example.finvest.modules.account.domain.repository.PeaRepository
import com.example.finvest.modules.shared.application.manager.TransactionManager

class UpdatePeaUseCase(
    private val accountRepository: AccountRepository,
    private val repository: PeaRepository,
    private val transactionManager: TransactionManager,
    private val logger: Logger,
) {
    operator fun invoke(accountWithDetails: AccountWithDetails<Pea>): AccountWithDetails<Pea> {
        logger.info("start invoke")
        val result =
            transactionManager.execute {
                val account = accountRepository.updateAccount(accountWithDetails.account)
                val pea = repository.updatePea(accountWithDetails.details)
                return@execute AccountWithDetails(account, pea)
            }
        logger.info("finis invoke")
        return result
    }
}
