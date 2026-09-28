package com.example.finvest.modules.account.application.usecase

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.domain.models.AccountWithDetails
import com.example.finvest.modules.account.domain.models.Pee
import com.example.finvest.modules.account.domain.repository.AccountRepository
import com.example.finvest.modules.account.domain.repository.PeeRepository
import com.example.finvest.modules.shared.application.manager.TransactionManager

class UpdatePeeUseCase(
    private val accountRepository: AccountRepository,
    private val repository: PeeRepository,
    private val transactionManager: TransactionManager,
    private val logger: Logger,
) {
    operator fun invoke(accountWithDetails: AccountWithDetails<Pee>): AccountWithDetails<Pee> {
        logger.info("start invoke")
        val result =
            transactionManager.execute {
                val account = accountRepository.updateAccount(accountWithDetails.account)
                val pee = repository.updatePee(accountWithDetails.details)
                return@execute AccountWithDetails(account, pee)
            }
        logger.info("finis invoke")
        return result
    }
}
