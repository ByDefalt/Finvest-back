package com.example.finvest.account.service

import com.example.finvest.account.domain.Account
import com.example.finvest.account.domain.AccountType
import com.example.finvest.account.dto.AccountCreateRequest
import com.example.finvest.account.dto.AccountUpdateRequest
import com.example.finvest.account.repository.AccountRepository
import com.example.finvest.common.logger.Logger
import org.springframework.stereotype.Service

@Service
class AccountService(
    private val accountRepository: AccountRepository,
    private val logger: Logger
) {
    fun getAllMyAccounts(userID: Long): List<Account> {
        logger.info("Fetching all accounts for user with id: $userID")
        return accountRepository.findAllByUserId(userID)
    }

    fun createAccount(account: AccountCreateRequest, userID: Long): Account {
        logger.info("Creating new account for user with id: $userID")
        return accountRepository.createAccount(
            Account(
                id = 0,
                name = account.name,
                balance = account.balance,
                type = AccountType.valueOf(account.type),
                userId = userID
            )
        )
    }

    fun updateAccount(account: AccountUpdateRequest, userID: Long): Account {
        logger.info("Updating account for user with id: $userID")
        return accountRepository.updateAccount(
            Account(
                id = account.id,
                name = account.name,
                balance = account.balance,
                type = AccountType.valueOf(account.type),
                userId = userID
            )
        )
    }

    fun deleteAccount(id: Long, userID: Long) {
        logger.info("Deleting account with id: $id for user with id: $userID")
        accountRepository.deleteAccount(id, userID)
    }

}