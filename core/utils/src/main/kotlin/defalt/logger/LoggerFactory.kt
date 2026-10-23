package defalt.logger

/** Creates the loggers of the application. */
interface LoggerFactory {
    /**
     * Logger named after [type].
     *
     * @param type class that logs.
     * @return a logger named after [type].
     */
    fun getLogger(type: Class<*>): Logger
}
