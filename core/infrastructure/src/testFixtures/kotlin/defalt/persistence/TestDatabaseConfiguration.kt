package defalt.persistence

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Import

/** Database of the integration tests: [MariaDbTestConfiguration] by default. */
@TestConfiguration(proxyBeanMethods = false)
@Import(MariaDbTestConfiguration::class)
class TestDatabaseConfiguration
