package com.example.finvest.logger

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ConsoleLogger(
    private val name: String,
    private val level: LogLevel,
) : Logger {
    private val formatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    private fun isEnabled(logLevel: LogLevel): Boolean = logLevel.priority >= level.priority

    private fun timestamp(): String = LocalDateTime.now().format(formatter)

    private fun format(
        level: LogLevel,
        message: String,
    ): String = "[${timestamp()}] ${level.name} [$name] - $message"

    override fun trace(message: String) {
        log(LogLevel.TRACE, message)
    }

    override fun debug(message: String) {
        log(LogLevel.DEBUG, message)
    }

    override fun info(message: String) {
        log(LogLevel.INFO, message)
    }

    override fun warn(message: String) {
        log(LogLevel.WARN, message)
    }

    override fun error(message: String) {
        log(LogLevel.ERROR, message)
    }

    override fun error(
        message: String,
        t: Throwable,
    ) {
        if (!isEnabled(LogLevel.ERROR)) return

        System.err.println(format(LogLevel.ERROR, message))
        t.printStackTrace(System.err)
    }

    override fun isLevelEnabled(level: LogLevel): Boolean = isEnabled(level)

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
