package com.example.finvest.common.logger

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class LoggerConfig {

    @Bean
    fun logger(
        @Value("\${app.logger.enabled}") enabled: Boolean = false,
        @Value("\${app.logger.level}") level: LogLevel = LogLevel.ERROR
    ): Logger {
        return if (enabled) {
            ConsoleLogger(level)
        } else {
            NullLogger()
        }
    }
}