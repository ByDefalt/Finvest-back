package defalt.manager

/** Runs the block directly. Rollback is not simulated: it is covered by the integration tests. */
class ImmediateTransactionManager : TransactionManager {
    /** Number of transactions opened so far. */
    var executions = 0
        private set

    /**
     * Runs [block] in a read-write transaction, committed if the block succeeds and rolled back otherwise.
     *
     * @param block database work to run, whose result is returned.
     * @return the result of the block, or the failure that rolled the transaction back.
     */
    override fun <T> execute(block: () -> T): Result<T> {
        executions++
        return runCatching(block)
    }

    /**
     * Runs [block] in a read-only transaction: a consistent snapshot of the data, writes are rejected.
     *
     * @param block database reads to run, whose result is returned.
     * @return the result of the block, or its failure.
     */
    override fun <T> executeReadOnly(block: () -> T): Result<T> {
        executions++
        return runCatching(block)
    }
}
