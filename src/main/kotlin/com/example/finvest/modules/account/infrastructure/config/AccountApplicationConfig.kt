package com.example.finvest.modules.account.infrastructure.config

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.application.usecase.GetDashboardUseCase
import com.example.finvest.modules.account.domain.repository.AccountRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AccountApplicationConfig {

    @Bean
    fun getDashboardUseCase(
        accountRepository: AccountRepository,
        logger: Logger
    ): GetDashboardUseCase {
        return GetDashboardUseCase(
            accountRepository = accountRepository,
            logger = logger
        )
    }
}