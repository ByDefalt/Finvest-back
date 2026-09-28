package defalt.manager

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class SpringTransactionManager : TransactionManager {
    @Transactional
    override fun <T> execute(block: () -> T): Result<T> = runCatching { block() }
}
