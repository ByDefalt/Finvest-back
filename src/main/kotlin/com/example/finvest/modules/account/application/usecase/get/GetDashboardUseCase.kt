package com.example.finvest.modules.account.application.usecase.get

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.application.solver.DashboardRepositorySolver
import com.example.finvest.modules.account.domain.models.AccountWithDetails
import com.example.finvest.modules.account.domain.repository.AccountRepository

class GetDashboardUseCase(
    private val accountRepository: AccountRepository,
    private val dashboardRepositorySolverList: List<DashboardRepositorySolver<*>>,
    private val logger: Logger,
) {
    operator fun invoke(userId: Long): List<AccountWithDetails<*>> {
        logger.info("Fetching dashboard data")
        val accounts = accountRepository.getAccountByUserId(userId)
        val accountListWithDetails =
            dashboardRepositorySolverList.flatMap { solver ->
                solver.solve(userId = userId, accountList = accounts)
            }
        logger.info("Dashboard data fetched: ${accountListWithDetails.size} accounts with details")
        return accountListWithDetails
    }
}
