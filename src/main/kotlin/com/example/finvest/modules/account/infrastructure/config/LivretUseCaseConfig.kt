package com.example.finvest.modules.account.infrastructure.config

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.application.usecase.create.CreateLivretUseCase
import com.example.finvest.modules.account.application.usecase.delete.DeleteLivretUseCase
import com.example.finvest.modules.account.application.usecase.get.GetLivretByUserIdUseCase
import com.example.finvest.modules.account.application.usecase.update.UpdateLivretUseCase
import com.example.finvest.modules.account.domain.repository.AccountRepository
import com.example.finvest.modules.account.domain.repository.LivretRepository
import com.example.finvest.modules.shared.application.manager.TransactionManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class LivretUseCaseConfig {
    @Bean
    fun getLivretByUserIdUseCase(
        repository: LivretRepository,
        logger: Logger,
    ): GetLivretByUserIdUseCase = GetLivretByUserIdUseCase(repository, logger)

    @Bean
    fun createLivretUseCase(
        accountRepository: AccountRepository,
        repository: LivretRepository,
        transactionManager: TransactionManager,
        logger: Logger,
    ): CreateLivretUseCase = CreateLivretUseCase(accountRepository, repository, transactionManager, logger)

    @Bean
    fun updateLivretUseCase(
        accountRepository: AccountRepository,
        repository: LivretRepository,
        transactionManager: TransactionManager,
        logger: Logger,
    ): UpdateLivretUseCase = UpdateLivretUseCase(accountRepository, repository, transactionManager, logger)

    @Bean
    fun deleteLivretUseCase(
        repository: LivretRepository,
        logger: Logger,
    ): DeleteLivretUseCase = DeleteLivretUseCase(repository, logger)
}
