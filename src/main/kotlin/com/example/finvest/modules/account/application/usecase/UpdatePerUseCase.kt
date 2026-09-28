package com.example.finvest.modules.account.application.usecase

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.domain.models.AccountWithDetails
import com.example.finvest.modules.account.domain.models.Per
import com.example.finvest.modules.account.domain.repository.AccountRepository
import com.example.finvest.modules.account.domain.repository.PerRepository
import com.example.finvest.modules.shared.application.manager.TransactionManager

class UpdatePerUseCase(
    private val accountRepository: AccountRepository,
    private val repository: PerRepository,
    private val transactionManager: TransactionManager,
    private val logger: Logger,
) {
    operator fun invoke(accountWithDetails: AccountWithDetails<Per>): AccountWithDetails<Per> {
        logger.info("start invoke")
        val result =
            transactionManager.execute {
                val account = accountRepository.updateAccount(accountWithDetails.account)
                val per = repository.updatePer(accountWithDetails.details)
                return@execute AccountWithDetails(account, per)
            }
        logger.info("finis invoke")
        return result
    }
}
