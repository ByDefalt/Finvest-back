package com.example.finvest.modules.account.infrastructure.config

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.application.usecase.CreateCompteCourantUseCase
import com.example.finvest.modules.account.application.usecase.DeleteCompteCourantUseCase
import com.example.finvest.modules.account.application.usecase.GetCompteCourantByUserIdUseCase
import com.example.finvest.modules.account.application.usecase.UpdateCompteCourantUseCase
import com.example.finvest.modules.account.domain.repository.AccountRepository
import com.example.finvest.modules.account.domain.repository.CompteCourantRepository
import com.example.finvest.modules.shared.application.manager.TransactionManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CompteCourantUseCaseConfig {
    @Bean
    fun getCompteCourantByUserIdUseCase(
        repository: CompteCourantRepository,
        logger: Logger,
    ): GetCompteCourantByUserIdUseCase = GetCompteCourantByUserIdUseCase(repository, logger)

    @Bean
    fun createCompteCourantUseCase(
        accountRepository: AccountRepository,
        repository: CompteCourantRepository,
        transactionManager: TransactionManager,
        logger: Logger,
    ): CreateCompteCourantUseCase = CreateCompteCourantUseCase(accountRepository, repository, transactionManager, logger)

    @Bean
    fun updateCompteCourantUseCase(
        accountRepository: AccountRepository,
        repository: CompteCourantRepository,
        transactionManager: TransactionManager,
        logger: Logger,
    ): UpdateCompteCourantUseCase = UpdateCompteCourantUseCase(accountRepository, repository, transactionManager, logger)

    @Bean
    fun deleteCompteCourantUseCase(
        repository: CompteCourantRepository,
        logger: Logger,
    ): DeleteCompteCourantUseCase = DeleteCompteCourantUseCase(repository, logger)
}
