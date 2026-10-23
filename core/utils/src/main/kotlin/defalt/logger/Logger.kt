package defalt.logger

/** Logging abstraction used by the whole application, so that no layer depends on a logging framework. */
interface Logger {
    /**
     * Logs a detailed diagnostic message.
     *
     * @param message message to log.
     */
    fun trace(message: String)

    /**
     * Logs a debugging message.
     *
     * @param message message to log.
     */
    fun debug(message: String)

    /**
     * Logs an informative message.
     *
     * @param message message to log.
     */
    fun info(message: String)

    /**
     * Logs an unusual but handled situation.
     *
     * @param message message to log.
     */
    fun warn(message: String)

    /**
     * Logs an error.
     *
     * @param message message to log.
     */
    fun error(message: String)

    /**
     * Logs an error with the stack trace of [t].
     *
     * @param message message to log.
     * @param t error whose stack trace is logged.
     */
    fun error(
        message: String,
        t: Throwable,
    )

    /**
     * Whether the messages of [level] are written, to avoid building costly messages for nothing.
     *
     * @param level level to check.
     * @return true if the messages of [level] are written.
     */
    fun isLevelEnabled(level: LogLevel): Boolean
}
