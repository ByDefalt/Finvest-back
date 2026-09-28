package com.example.finvest.modules.account.infrastructure.config

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.application.usecase.CreatePeaUseCase
import com.example.finvest.modules.account.application.usecase.DeletePeaUseCase
import com.example.finvest.modules.account.application.usecase.GetPeaByUserIdUseCase
import com.example.finvest.modules.account.application.usecase.UpdatePeaUseCase
import com.example.finvest.modules.account.domain.repository.AccountRepository
import com.example.finvest.modules.account.domain.repository.PeaRepository
import com.example.finvest.modules.shared.application.manager.TransactionManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PeaUseCaseConfig {
    @Bean
    fun getPeaByUserIdUseCase(
        repository: PeaRepository,
        logger: Logger,
    ): GetPeaByUserIdUseCase = GetPeaByUserIdUseCase(repository, logger)

    @Bean
    fun createPeaUseCase(
        accountRepository: AccountRepository,
        repository: PeaRepository,
        transactionManager: TransactionManager,
        logger: Logger,
    ): CreatePeaUseCase = CreatePeaUseCase(accountRepository, repository, transactionManager, logger)

    @Bean
    fun updatePeaUseCase(
        accountRepository: AccountRepository,
        repository: PeaRepository,
        transactionManager: TransactionManager,
        logger: Logger,
    ): UpdatePeaUseCase = UpdatePeaUseCase(accountRepository, repository, transactionManager, logger)

    @Bean
    fun deletePeaUseCase(
        repository: PeaRepository,
        logger: Logger,
    ): DeletePeaUseCase = DeletePeaUseCase(repository, logger)
}
