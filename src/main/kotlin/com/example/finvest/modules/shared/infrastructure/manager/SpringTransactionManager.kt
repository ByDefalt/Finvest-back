package com.example.finvest.modules.shared.infrastructure.manager

import com.example.finvest.modules.shared.application.manager.TransactionManager
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class SpringTransactionManager : TransactionManager {
    @Transactional
    override fun <T> execute(block: () -> T): T = block()
}
