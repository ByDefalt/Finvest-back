package com.example.finvest.logger

import org.springframework.beans.factory.InjectionPoint
import org.springframework.beans.factory.annotation.Value
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope

@Configuration
class LoggerConfig {

    @Bean
    fun loggerFactory(
        @Value("\${app.logger.enabled}") enabled: Boolean = false,
        @Value("\${app.logger.level}") level: LogLevel = LogLevel.ERROR
    ): LoggerFactory {
        return DefaultLoggerFactory(
            enabled = enabled,
            level = level
        )
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    fun logger(loggerFactory: LoggerFactory, injectionPoint: InjectionPoint): Logger =
        loggerFactory.getLogger(injectionPoint.member.declaringClass)
}