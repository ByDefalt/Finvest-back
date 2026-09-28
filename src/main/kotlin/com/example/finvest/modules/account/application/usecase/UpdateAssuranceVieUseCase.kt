package com.example.finvest.modules.account.application.usecase

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.domain.models.AccountWithDetails
import com.example.finvest.modules.account.domain.models.AssuranceVie
import com.example.finvest.modules.account.domain.repository.AccountRepository
import com.example.finvest.modules.account.domain.repository.AssuranceVieRepository
import com.example.finvest.modules.shared.application.manager.TransactionManager

class UpdateAssuranceVieUseCase(
    private val accountRepository: AccountRepository,
    private val repository: AssuranceVieRepository,
    private val transactionManager: TransactionManager,
    private val logger: Logger,
) {
    operator fun invoke(accountWithDetails: AccountWithDetails<AssuranceVie>): AccountWithDetails<AssuranceVie> {
        logger.info("start invoke")
        val result =
            transactionManager.execute {
                val account = accountRepository.updateAccount(accountWithDetails.account)
                val assuranceVie = repository.updateAssuranceVie(accountWithDetails.details)
                return@execute AccountWithDetails(account, assuranceVie)
            }
        logger.info("finis invoke")
        return result
    }
}
