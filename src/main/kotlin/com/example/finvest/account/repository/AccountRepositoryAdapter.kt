package com.example.finvest.account.repository

import com.example.finvest.account.domain.AccountDashboardData
import com.example.finvest.account.mapper.toDomain
import com.example.finvest.common.logger.Logger
import org.springframework.stereotype.Repository

@Repository
class AccountRepositoryAdapter(
    private val accountJdbcRepository: AccountJdbcRepository,
    private val logger: Logger
) : AccountRepository {


    override fun findDashboardByUserId(userId: Long): List<AccountDashboardData> {
        return accountJdbcRepository.findDashboardByUserId(userId).toDomain()
    }

}