package defalt.persistence

import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.AutoConfigurationPackage

/**
 * Bootstrap configuration of the JDBC slice tests ([defalt.persistence.repository.JdbcRepositoryTest])
 * for modules that do not contain the Spring Boot application class.
 */
@SpringBootConfiguration
@AutoConfigurationPackage
class PersistenceTestApplication
