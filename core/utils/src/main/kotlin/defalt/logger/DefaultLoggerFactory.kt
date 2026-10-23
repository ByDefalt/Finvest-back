package defalt.logger

/**
 * Creates [ConsoleLogger]s when logging is [enabled], [NullLogger]s otherwise.
 *
 * @param enabled whether the loggers write their messages.
 * @param level minimum level of the written messages.
 */
class DefaultLoggerFactory(
    private val enabled: Boolean,
    private val level: LogLevel,
) : LoggerFactory {
    /**
     * Logger named after [type].
     *
     * @param type class that logs.
     * @return a logger named after [type].
     */
    override fun getLogger(type: Class<*>): Logger =
        if (enabled) {
            ConsoleLogger(
                name = type.simpleName,
                level = level,
            )
        } else {
            NullLogger()
        }
}
