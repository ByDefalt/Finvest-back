package com.example.finvest.modules.account.application.usecase

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.domain.models.AccountWithDetails
import com.example.finvest.modules.account.domain.models.Pee
import com.example.finvest.modules.account.domain.repository.AccountRepository
import com.example.finvest.modules.account.domain.repository.PeeRepository
import com.example.finvest.modules.account.domain.valueobject.AccountId
import com.example.finvest.modules.shared.application.manager.TransactionManager

class CreatePeeUseCase(
    private val accountRepository: AccountRepository,
    private val repository: PeeRepository,
    private val transactionManager: TransactionManager,
    private val logger: Logger,
) {
    operator fun invoke(accountWithDetails: AccountWithDetails<Pee>): AccountId {
        logger.info("start invoke")
        val result =
            transactionManager.execute {
                val accountId = accountRepository.createAccount(accountWithDetails.account)
                return@execute repository.createPee(accountWithDetails.details.copy(accountId = accountId))
            }
        logger.info("finis invoke")
        return result
    }
}
