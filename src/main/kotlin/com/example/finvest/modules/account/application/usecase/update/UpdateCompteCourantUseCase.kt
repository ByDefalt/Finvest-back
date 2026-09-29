package com.example.finvest.modules.account.application.usecase.update

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.domain.models.AccountWithDetails
import com.example.finvest.modules.account.domain.models.CompteCourant
import com.example.finvest.modules.account.domain.repository.AccountRepository
import com.example.finvest.modules.account.domain.repository.CompteCourantRepository
import com.example.finvest.modules.shared.application.manager.TransactionManager

class UpdateCompteCourantUseCase(
    private val accountRepository: AccountRepository,
    private val repository: CompteCourantRepository,
    private val transactionManager: TransactionManager,
    private val logger: Logger,
) {
    operator fun invoke(accountWithDetails: AccountWithDetails<CompteCourant>): AccountWithDetails<CompteCourant> {
        logger.info("start invoke")
        val result =
            transactionManager.execute {
                val account = accountRepository.updateAccount(accountWithDetails.account)
                val compteCourant = repository.updateCompteCourant(accountWithDetails.details)
                return@execute AccountWithDetails(account, compteCourant)
            }
        logger.info("finis invoke")
        return result
    }
}
