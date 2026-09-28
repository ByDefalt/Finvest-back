package com.example.finvest.modules.account.application.usecase

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.domain.models.AccountWithDetails
import com.example.finvest.modules.account.domain.models.CompteTitre
import com.example.finvest.modules.account.domain.repository.AccountRepository
import com.example.finvest.modules.account.domain.repository.CompteTitreRepository
import com.example.finvest.modules.shared.application.manager.TransactionManager

class UpdateCompteTitreUseCase(
    private val accountRepository: AccountRepository,
    private val repository: CompteTitreRepository,
    private val transactionManager: TransactionManager,
    private val logger: Logger,
) {
    operator fun invoke(accountWithDetails: AccountWithDetails<CompteTitre>): AccountWithDetails<CompteTitre> {
        logger.info("start invoke")
        val result =
            transactionManager.execute {
                val account = accountRepository.updateAccount(accountWithDetails.account)
                val compteTitre = repository.updateCompteTitre(accountWithDetails.details)
                return@execute AccountWithDetails(account, compteTitre)
            }
        logger.info("finis invoke")
        return result
    }
}
