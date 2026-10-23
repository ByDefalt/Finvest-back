package defalt.logger

/** Logger that discards every message: used when logging is disabled and in the tests. */
class NullLogger : Logger {
    /**
     * Logs a detailed diagnostic message.
     *
     * @param message message to log.
     */
    override fun trace(message: String) = Unit

    /**
     * Logs a debugging message.
     *
     * @param message message to log.
     */
    override fun debug(message: String) = Unit

    /**
     * Logs an informative message.
     *
     * @param message message to log.
     */
    override fun info(message: String) = Unit

    /**
     * Logs an unusual but handled situation.
     *
     * @param message message to log.
     */
    override fun warn(message: String) = Unit

    /**
     * Logs an error.
     *
     * @param message message to log.
     */
    override fun error(message: String) = Unit

    /**
     * Logs an error with the stack trace of [t].
     *
     * @param message message to log.
     * @param t error whose stack trace is logged.
     */
    override fun error(
        message: String,
        t: Throwable,
    ) = Unit

    /**
     * Whether the messages of [level] are written, to avoid building costly messages for nothing.
     *
     * @param level level to check.
     * @return true if the messages of [level] are written.
     */
    override fun isLevelEnabled(level: LogLevel): Boolean = false
}
