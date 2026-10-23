package defalt.manager

/**
 * Port of the transactions: use cases run their database work through it, without depending on Spring.
 * Failures are returned in the [Result] instead of being thrown.
 */
interface TransactionManager {
    /**
     * Runs [block] in a read-write transaction, committed if the block succeeds and rolled back otherwise.
     *
     * @param block database work to run, whose result is returned.
     * @return the result of the block, or the failure that rolled the transaction back.
     */
    fun <T> execute(block: () -> T): Result<T>

    /**
     * Runs [block] in a read-only transaction: a consistent snapshot of the data, writes are rejected.
     *
     * @param block database reads to run, whose result is returned.
     * @return the result of the block, or its failure.
     */
    fun <T> executeReadOnly(block: () -> T): Result<T>
}
