package com.example.finvest.modules.shared.infrastructure.persistence

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.testcontainers.containers.MariaDBContainer

@TestConfiguration(proxyBeanMethods = false)
@ConditionalOnProperty("test.db", havingValue = "mariadb", matchIfMissing = true)
class MariaDbTestConfiguration {
    @Bean
    @ServiceConnection
    fun mariaDb() = MariaDBContainer("mariadb:11.8")
}
