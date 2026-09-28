package com.example.finvest.modules.account.infrastructure.config

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.application.usecase.CreatePeeUseCase
import com.example.finvest.modules.account.application.usecase.DeletePeeUseCase
import com.example.finvest.modules.account.application.usecase.GetPeeByUserIdUseCase
import com.example.finvest.modules.account.application.usecase.UpdatePeeUseCase
import com.example.finvest.modules.account.domain.repository.AccountRepository
import com.example.finvest.modules.account.domain.repository.PeeRepository
import com.example.finvest.modules.shared.application.manager.TransactionManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PeeUseCaseConfig {
    @Bean
    fun getPeeByUserIdUseCase(
        repository: PeeRepository,
        logger: Logger,
    ): GetPeeByUserIdUseCase = GetPeeByUserIdUseCase(repository, logger)

    @Bean
    fun createPeeUseCase(
        accountRepository: AccountRepository,
        repository: PeeRepository,
        transactionManager: TransactionManager,
        logger: Logger,
    ): CreatePeeUseCase = CreatePeeUseCase(accountRepository, repository, transactionManager, logger)

    @Bean
    fun updatePeeUseCase(
        accountRepository: AccountRepository,
        repository: PeeRepository,
        transactionManager: TransactionManager,
        logger: Logger,
    ): UpdatePeeUseCase = UpdatePeeUseCase(accountRepository, repository, transactionManager, logger)

    @Bean
    fun deletePeeUseCase(
        repository: PeeRepository,
        logger: Logger,
    ): DeletePeeUseCase = DeletePeeUseCase(repository, logger)
}
