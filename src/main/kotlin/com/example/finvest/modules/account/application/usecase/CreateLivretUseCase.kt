package com.example.finvest.modules.account.application.usecase

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.domain.models.AccountWithDetails
import com.example.finvest.modules.account.domain.models.Livret
import com.example.finvest.modules.account.domain.repository.AccountRepository
import com.example.finvest.modules.account.domain.repository.LivretRepository
import com.example.finvest.modules.account.domain.valueobject.AccountId
import com.example.finvest.modules.shared.application.manager.TransactionManager

class CreateLivretUseCase(
    private val accountRepository: AccountRepository,
    private val repository: LivretRepository,
    private val transactionManager: TransactionManager,
    private val logger: Logger,
) {
    operator fun invoke(accountWithDetails: AccountWithDetails<Livret>): AccountId {
        logger.info("start invoke")
        val result =
            transactionManager.execute {
                val accountId = accountRepository.createAccount(accountWithDetails.account)
                return@execute repository.createLivret(accountWithDetails.details.copy(accountId = accountId))
            }
        logger.info("finis invoke")
        return result
    }
}
