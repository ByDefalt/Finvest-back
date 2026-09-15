package com.example.finvest.account.service

import com.example.finvest.account.domain.Account
import com.example.finvest.account.dto.AccountCreateRequest
import com.example.finvest.account.dto.AccountUpdateRequest
import com.example.finvest.account.repository.AccountRepository
import com.example.finvest.common.logger.Logger
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AccountService(
    private val accountRepository: AccountRepository,
    private val logger: Logger
) {

    fun getAllMyAccounts(userId: Long): List<Account> {
        logger.info("Fetching all accounts for user with id: $userId")
        return accountRepository.findAllByUserId(userId)
    }

    fun createAccount(account: AccountCreateRequest, userId: Long): Account {
        logger.info("Creating new account for user with id: $userId")

        return accountRepository.createAccount(
            Account(
                id = 0L,
                bankId = account.bankId,
                name = account.name,
                balance = account.balance,
                currencyId = account.currencyId,
                createdAt = LocalDateTime.now(),
                closedAt = null,
                accountStatusId = account.accountStatusId,
                description = account.description
            )
        )
    }

    fun updateAccount(account: AccountUpdateRequest, userId: Long): Account {
        logger.info("Updating account for user with id: $userId")

        val existingAccount = accountRepository.findByIdAndUserId(
            account.id,
            userId
        )

        return accountRepository.updateAccount(
            existingAccount.copy(
                bankId = account.bankId,
                name = account.name,
                balance = account.balance,
                currencyId = account.currencyId,
                accountStatusId = account.accountStatusId,
                closedAt = account.closedAt,
                description = account.description
            ),
            userId
        )
    }

    fun deleteAccount(id: Long, userId: Long) {
        logger.info("Deleting account with id: $id for user with id: $userId")
        accountRepository.deleteAccount(id, userId)
    }
}