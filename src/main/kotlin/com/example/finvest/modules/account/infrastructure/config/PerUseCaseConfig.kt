package com.example.finvest.modules.account.infrastructure.config

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.application.usecase.create.CreatePerUseCase
import com.example.finvest.modules.account.application.usecase.delete.DeletePerUseCase
import com.example.finvest.modules.account.application.usecase.get.GetPerByUserIdUseCase
import com.example.finvest.modules.account.application.usecase.update.UpdatePerUseCase
import com.example.finvest.modules.account.domain.repository.AccountRepository
import com.example.finvest.modules.account.domain.repository.PerRepository
import com.example.finvest.modules.shared.application.manager.TransactionManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PerUseCaseConfig {
    @Bean
    fun getPerByUserIdUseCase(
        repository: PerRepository,
        logger: Logger,
    ): GetPerByUserIdUseCase = GetPerByUserIdUseCase(repository, logger)

    @Bean
    fun createPerUseCase(
        accountRepository: AccountRepository,
        repository: PerRepository,
        transactionManager: TransactionManager,
        logger: Logger,
    ): CreatePerUseCase = CreatePerUseCase(accountRepository, repository, transactionManager, logger)

    @Bean
    fun updatePerUseCase(
        accountRepository: AccountRepository,
        repository: PerRepository,
        transactionManager: TransactionManager,
        logger: Logger,
    ): UpdatePerUseCase = UpdatePerUseCase(accountRepository, repository, transactionManager, logger)

    @Bean
    fun deletePerUseCase(
        repository: PerRepository,
        logger: Logger,
    ): DeletePerUseCase = DeletePerUseCase(repository, logger)
}
