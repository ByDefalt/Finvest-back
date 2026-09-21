package com.example.finvest.logger


class NullLogger : Logger {
    override fun trace(message: String) = Unit
    override fun debug(message: String) = Unit
    override fun info(message: String) = Unit
    override fun warn(message: String) = Unit
    override fun error(message: String) = Unit
    override fun error(message: String, t: Throwable) = Unit
    override fun isLevelEnabled(level: LogLevel): Boolean = false
}
