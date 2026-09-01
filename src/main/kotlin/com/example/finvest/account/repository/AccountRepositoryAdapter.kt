package com.example.finvest.account.repository

import com.example.finvest.account.domain.Account
import com.example.finvest.account.mapper.toDomain
import com.example.finvest.account.mapper.toEntity
import com.example.finvest.common.logger.Logger
import org.springframework.stereotype.Repository

@Repository
class AccountRepositoryAdapter(
    private val accountJdbcRepository: AccountJdbcRepository,
    private val logger: Logger
) : AccountRepository {


    override fun findAllByUserId(userId: Long): List<Account> {
        return accountJdbcRepository.findAllByUserId(userId).toDomain()
    }

    override fun createAccount(account: Account): Account {
        return accountJdbcRepository.insertAccount(account.toEntity()).toDomain()
    }

    override fun updateAccount(account: Account): Account {
        return accountJdbcRepository.updateAccount(account.toEntity()).toDomain()
    }

    override fun deleteAccount(id: Long, userId: Long) {
        accountJdbcRepository.deleteByIdAndUserId(id, userId)
    }

}