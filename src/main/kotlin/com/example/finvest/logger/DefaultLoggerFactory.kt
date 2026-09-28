package com.example.finvest.logger

class DefaultLoggerFactory(
    private val enabled: Boolean,
    private val level: LogLevel,
) : LoggerFactory {
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
