package com.example.finvest.modules.account.application.usecase

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.domain.models.AccountDashboardData
import com.example.finvest.modules.account.domain.repository.AccountRepository
import com.example.finvest.modules.shared.application.security.AuthenticatedUser

class GetDashboardUseCase(
    private val accountRepository: AccountRepository,
    private val logger: Logger
) {
    operator fun invoke(authenticatedUser: AuthenticatedUser): List<AccountDashboardData> {
        return accountRepository.findDashboardByUserId(authenticatedUser.id)
    }
}