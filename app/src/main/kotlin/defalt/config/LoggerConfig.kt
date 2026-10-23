package defalt.config

import defalt.logger.DefaultLoggerFactory
import defalt.logger.LogLevel
import defalt.logger.Logger
import defalt.logger.LoggerFactory
import org.springframework.beans.factory.InjectionPoint
import org.springframework.beans.factory.annotation.Value
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope

/** Provides the [Logger] injected in every component of the application. */
@Configuration
class LoggerConfig {
    /**
     * Factory of the application loggers.
     *
     * @param enabled whether logs are written (`app.logger.enabled`), disabled by default.
     * @param level minimum level of the written logs (`app.logger.level`), [LogLevel.ERROR] by default.
     * @return a factory of console loggers if logging is enabled, of silent loggers otherwise.
     */
    @Bean
    fun loggerFactory(
        @Value("\${app.logger.enabled}") enabled: Boolean = false,
        @Value("\${app.logger.level}") level: LogLevel = LogLevel.ERROR,
    ): LoggerFactory =
        DefaultLoggerFactory(
            enabled = enabled,
            level = level,
        )

    /**
     * Logger of the class that requests it, named after that class.
     * Prototype scoped: every injection point gets its own instance.
     *
     * @param loggerFactory factory creating the logger.
     * @param injectionPoint where the logger is injected, which gives the class that logs.
     * @return a logger named after the class that requests it.
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    fun logger(
        loggerFactory: LoggerFactory,
        injectionPoint: InjectionPoint,
    ): Logger = loggerFactory.getLogger(injectionPoint.member.declaringClass)
}
