package com.example.finvest.modules.account.infrastructure.config

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.application.solver.DashboardRepositorySolver
import com.example.finvest.modules.account.application.usecase.get.GetDashboardUseCase
import com.example.finvest.modules.account.domain.repository.AccountRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DashboardUseCaseConfig {
    @Bean
    fun getDashboardUseCase(
        accountRepository: AccountRepository,
        dashboardRepositorySolverList: List<DashboardRepositorySolver<*>>,
        logger: Logger,
    ): GetDashboardUseCase =
        GetDashboardUseCase(
            accountRepository = accountRepository,
            dashboardRepositorySolverList = dashboardRepositorySolverList,
            logger = logger,
        )
}
