package com.example.finvest.common.config

import com.example.finvest.common.logger.Logger
import org.h2.tools.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class H2Config(
    private val logger: Logger
) {

    @Bean
    fun h2Server(): Server? {
        return try {
            Server.createTcpServer(
                "-tcp",
                "-tcpAllowOthers",
                "-tcpPort", "9092"
            ).start()
        } catch (ex: Exception) {
            logger.error("Failed to start H2 server", ex)
            null
        }
    }
}