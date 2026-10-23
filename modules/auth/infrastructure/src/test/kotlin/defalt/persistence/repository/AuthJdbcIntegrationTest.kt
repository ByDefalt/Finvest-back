package defalt.persistence.repository

import defalt.logger.Logger
import defalt.logger.NullLogger
import defalt.persistence.api.UserQueryApiImpl
import defalt.valueobject.ExternalUserId
import defalt.valueobject.PersonalInformationId
import defalt.valueobject.UserId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.test.context.jdbc.Sql

/**
 * Base class of the auth JDBC tests, run against MariaDB (Testcontainers) with the reference data:
 * internal users romain@example.com and test@example.com, external user john.doe@example.com.
 */
@Import(
    AuthJdbcIntegrationTest.LoggerConfiguration::class,
    UserJdbcRepository::class,
    PersonalInformationJdbcRepository::class,
    ExternalUserJdbcRepository::class,
    UserQueryApiImpl::class,
)
@Sql("/db/test/reference_data.sql")
abstract class AuthJdbcIntegrationTest : JdbcRepositoryTest() {
    @Autowired
    protected lateinit var userRepository: UserJdbcRepository

    @Autowired
    protected lateinit var personalInformationRepository: PersonalInformationJdbcRepository

    @Autowired
    protected lateinit var externalUserRepository: ExternalUserJdbcRepository

    protected fun userId(email: String) =
        UserId(
            idOf(
                "SELECT u.id FROM users u JOIN personal_informations pi ON pi.id = u.personal_information_id WHERE pi.email = :email",
                email,
            ),
        )

    protected fun externalUserId(email: String) =
        ExternalUserId(
            idOf(
                "SELECT u.id FROM users_external u JOIN personal_informations pi ON pi.id = u.personal_information_id WHERE pi.email = :email",
                email,
            ),
        )

    protected fun personalInformationId(email: String) =
        PersonalInformationId(idOf("SELECT id FROM personal_informations WHERE email = :email", email))

    private fun idOf(
        sql: String,
        email: String,
    ): Long = jdbcTemplate.queryForObject(sql, MapSqlParameterSource("email", email), Long::class.java)!!

    @TestConfiguration(proxyBeanMethods = false)
    class LoggerConfiguration {
        @Bean
        fun logger(): Logger = NullLogger()
    }

    protected companion object {
        const val ROMAIN = "romain@example.com"
        const val TEST = "test@example.com"
        const val JOHN = "john.doe@example.com"
    }
}
