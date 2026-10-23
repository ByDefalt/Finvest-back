package defalt.logger

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Logger writing to the console: `[yyyy-MM-dd HH:mm:ss] LEVEL [name] - message`.
 * Warnings and errors go to the standard error, the other levels to the standard output.
 *
 * @param name name of the logger, usually the simple name of the class that logs.
 * @param level minimum level of the written messages.
 */
class ConsoleLogger(
    private val name: String,
    private val level: LogLevel,
) : Logger {
    private val formatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    /**
     * Whether the messages of [logLevel] are written by this logger.
     *
     * @param logLevel level of a message.
     * @return true if [logLevel] is at least the level of this logger.
     */
    private fun isEnabled(logLevel: LogLevel): Boolean = logLevel.priority >= level.priority

    /**
     * Current date and time, as written in the logs.
     *
     * @return the current time, formatted `yyyy-MM-dd HH:mm:ss`.
     */
    private fun timestamp(): String = LocalDateTime.now().format(formatter)

    /**
     * Formats a log line.
     *
     * @param level level of the message.
     * @param message message to log.
     * @return the line `[timestamp] LEVEL [name] - message`.
     */
    private fun format(
        level: LogLevel,
        message: String,
    ): String = "[${timestamp()}] ${level.name} [$name] - $message"

    /**
     * Logs a detailed diagnostic message.
     *
     * @param message message to log.
     */
    override fun trace(message: String) {
        log(LogLevel.TRACE, message)
    }

    /**
     * Logs a debugging message.
     *
     * @param message message to log.
     */
    override fun debug(message: String) {
        log(LogLevel.DEBUG, message)
    }

    /**
     * Logs an informative message.
     *
     * @param message message to log.
     */
    override fun info(message: String) {
        log(LogLevel.INFO, message)
    }

    /**
     * Logs an unusual but handled situation.
     *
     * @param message message to log.
     */
    override fun warn(message: String) {
        log(LogLevel.WARN, message)
    }

    /**
     * Logs an error.
     *
     * @param message message to log.
     */
    override fun error(message: String) {
        log(LogLevel.ERROR, message)
    }

    /**
     * Logs an error with the stack trace of [t].
     *
     * @param message message to log.
     * @param t error whose stack trace is logged.
     */
    override fun error(
        message: String,
        t: Throwable,
    ) {
        if (!isEnabled(LogLevel.ERROR)) return

        System.err.println(format(LogLevel.ERROR, message))
        t.printStackTrace(System.err)
    }

    /**
     * Whether the messages of [level] are written, to avoid building costly messages for nothing.
     *
     * @param level level to check.
     * @return true if the messages of [level] are written.
     */
    override fun isLevelEnabled(level: LogLevel): Boolean = isEnabled(level)

    /**
     * Writes [message] if [level] is enabled: to the standard error for warnings and errors, to the standard output otherwise.
     *
     * @param level level of the message.
     * @param message message to log.
     */
    private fun log(
        level: LogLevel,
        message: String,
    ) {
        if (!isEnabled(level)) return

        val output =
            if (level.priority >= LogLevel.WARN.priority) {
                System.err
            } else {
                System.out
            }

        output.println(format(level, message))
    }
}
