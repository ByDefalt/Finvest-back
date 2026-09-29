package com.example.finvest.modules.account.infrastructure.config

import com.example.finvest.modules.account.application.usecase.get.LoadReferenceDataUseCase
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class ReferenceDataStartupLoader(
    private val loadReferenceDataUseCase: LoadReferenceDataUseCase,
) : ApplicationRunner {
    override fun run(args: ApplicationArguments) {
        loadReferenceDataUseCase()
    }
}
