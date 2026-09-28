package com.example.finvest.logger

interface Logger {
    fun trace(message: String)

    fun debug(message: String)

    fun info(message: String)

    fun warn(message: String)

    fun error(message: String)

    fun error(
        message: String,
        t: Throwable,
    )

    fun isLevelEnabled(level: LogLevel): Boolean
}
