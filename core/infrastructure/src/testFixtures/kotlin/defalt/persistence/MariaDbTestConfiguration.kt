package defalt.persistence

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.testcontainers.mariadb.MariaDBContainer

/**
 * Starts a MariaDB container (Testcontainers) and connects the test datasource to it.
 * Active unless the `test.db` property selects another database.
 */
@TestConfiguration(proxyBeanMethods = false)
@ConditionalOnProperty("test.db", havingValue = "mariadb", matchIfMissing = true)
class MariaDbTestConfiguration {
    /**
     * MariaDB container, shared by the tests of the same Spring context.
     *
     * @return the MariaDB container, started by Spring and connected to the test datasource.
     */
    @Bean
    @ServiceConnection
    fun mariaDb() = MariaDBContainer("mariadb:11.8")
}
