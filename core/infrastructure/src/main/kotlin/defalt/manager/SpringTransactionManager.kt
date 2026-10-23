package defalt.manager

import defalt.exception.CommonErrorCode
import defalt.exception.CustomException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Component
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionDefinition
import org.springframework.transaction.support.TransactionTemplate

/**
 * Every write of the application goes through this manager: database constraint violations are
 * translated here into [CustomException]s, so that they reach the client with a translatable code.
 *
 * @param txManager Spring transaction manager of the datasource.
 */
@Component
class SpringTransactionManager(
    txManager: PlatformTransactionManager,
) : TransactionManager {
    private val writeTemplate = TransactionTemplate(txManager)

    private val readTemplate =
        TransactionTemplate(txManager).apply {
            isReadOnly = true
            isolationLevel = TransactionDefinition.ISOLATION_REPEATABLE_READ
        }

    /**
     * Runs [block] in a read-write transaction, committed if the block succeeds and rolled back otherwise.
     *
     * @param block database work to run, whose result is returned.
     * @return the result of the block, or the failure that rolled the transaction back.
     */
    @Suppress("UNCHECKED_CAST")
    override fun <T> execute(block: () -> T): Result<T> = runCatching { writeTemplate.execute { block() } as T }.translateFailure()

    /**
     * Runs [block] in a read-only transaction: a consistent snapshot of the data, writes are rejected.
     *
     * @param block database reads to run, whose result is returned.
     * @return the result of the block, or its failure.
     */
    @Suppress("UNCHECKED_CAST")
    override fun <T> executeReadOnly(block: () -> T): Result<T> = runCatching { readTemplate.execute { block() } as T }.translateFailure()

    /**
     * Replaces the failure of this result by its translation, see [translate].
     *
     * @receiver result of a transaction.
     * @return the same result, with its failure translated.
     */
    private fun <T> Result<T>.translateFailure(): Result<T> = exceptionOrNull()?.let { Result.failure(it.translate()) } ?: this

    /**
     * Translates the database constraint violations into [CustomException]s:
     * a duplicate unique value into `DUPLICATE`, any other constraint violation into `INTEGRITY_VIOLATION`.
     * Other throwables are kept as they are.
     *
     * @receiver failure of a transaction.
     * @return a [CustomException] for a constraint violation, the throwable itself otherwise.
     */
    private fun Throwable.translate(): Throwable =
        when (this) {
            is DuplicateKeyException -> CustomException(CommonErrorCode.DUPLICATE, "A unique value already exists", this)
            is DataIntegrityViolationException ->
                CustomException(CommonErrorCode.INTEGRITY_VIOLATION, "The operation violates a data constraint", this)
            else -> this
        }
}
