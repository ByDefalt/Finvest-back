package com.example.finvest.modules.account.infrastructure.config

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.application.usecase.create.CreateCompteTitreUseCase
import com.example.finvest.modules.account.application.usecase.delete.DeleteCompteTitreUseCase
import com.example.finvest.modules.account.application.usecase.get.GetCompteTitreByUserIdUseCase
import com.example.finvest.modules.account.application.usecase.update.UpdateCompteTitreUseCase
import com.example.finvest.modules.account.domain.repository.AccountRepository
import com.example.finvest.modules.account.domain.repository.CompteTitreRepository
import com.example.finvest.modules.shared.application.manager.TransactionManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CompteTitreUseCaseConfig {
    @Bean
    fun getCompteTitreByUserIdUseCase(
        repository: CompteTitreRepository,
        logger: Logger,
    ): GetCompteTitreByUserIdUseCase = GetCompteTitreByUserIdUseCase(repository, logger)

    @Bean
    fun createCompteTitreUseCase(
        accountRepository: AccountRepository,
        repository: CompteTitreRepository,
        transactionManager: TransactionManager,
        logger: Logger,
    ): CreateCompteTitreUseCase = CreateCompteTitreUseCase(accountRepository, repository, transactionManager, logger)

    @Bean
    fun updateCompteTitreUseCase(
        accountRepository: AccountRepository,
        repository: CompteTitreRepository,
        transactionManager: TransactionManager,
        logger: Logger,
    ): UpdateCompteTitreUseCase = UpdateCompteTitreUseCase(accountRepository, repository, transactionManager, logger)

    @Bean
    fun deleteCompteTitreUseCase(
        repository: CompteTitreRepository,
        logger: Logger,
    ): DeleteCompteTitreUseCase = DeleteCompteTitreUseCase(repository, logger)
}
