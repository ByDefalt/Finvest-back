package com.example.finvest.modules.account.infrastructure.config

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.application.usecase.create.CreateAssuranceVieUseCase
import com.example.finvest.modules.account.application.usecase.delete.DeleteAssuranceVieUseCase
import com.example.finvest.modules.account.application.usecase.get.GetAssuranceVieByUserIdUseCase
import com.example.finvest.modules.account.application.usecase.update.UpdateAssuranceVieUseCase
import com.example.finvest.modules.account.domain.repository.AccountRepository
import com.example.finvest.modules.account.domain.repository.AssuranceVieRepository
import com.example.finvest.modules.shared.application.manager.TransactionManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AssuranceVieUseCaseConfig {
    @Bean
    fun getAssuranceVieByUserIdUseCase(
        repository: AssuranceVieRepository,
        logger: Logger,
    ): GetAssuranceVieByUserIdUseCase = GetAssuranceVieByUserIdUseCase(repository, logger)

    @Bean
    fun createAssuranceVieUseCase(
        accountRepository: AccountRepository,
        repository: AssuranceVieRepository,
        transactionManager: TransactionManager,
        logger: Logger,
    ): CreateAssuranceVieUseCase = CreateAssuranceVieUseCase(accountRepository, repository, transactionManager, logger)

    @Bean
    fun updateAssuranceVieUseCase(
        accountRepository: AccountRepository,
        repository: AssuranceVieRepository,
        transactionManager: TransactionManager,
        logger: Logger,
    ): UpdateAssuranceVieUseCase = UpdateAssuranceVieUseCase(accountRepository, repository, transactionManager, logger)

    @Bean
    fun deleteAssuranceVieUseCase(
        repository: AssuranceVieRepository,
        logger: Logger,
    ): DeleteAssuranceVieUseCase = DeleteAssuranceVieUseCase(repository, logger)
}
