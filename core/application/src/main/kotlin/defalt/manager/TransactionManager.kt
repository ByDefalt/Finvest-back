package defalt.manager

interface TransactionManager {
    fun <T> execute(block: () -> T): Result<T>
}
