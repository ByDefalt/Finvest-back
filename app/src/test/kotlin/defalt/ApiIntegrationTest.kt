package defalt

import defalt.persistence.RealDatabaseTest
import defalt.service.TokenGenerator
import defalt.valueobject.Email
import defalt.valueobject.UserId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc

/**
 * Base class of the end-to-end tests: the whole application against MariaDB (Testcontainers).
 * The database is reset and filled with the reference data before each test, and every subclass
 * shares the same Spring context, hence the same container.
 */
@SpringBootTest(classes = [BackApplication::class])
@AutoConfigureMockMvc
@RealDatabaseTest
@Sql(scripts = ["/db/dev/reset.sql", "/db/test/reference_data.sql"])
abstract class ApiIntegrationTest {
    @Autowired
    protected lateinit var mockMvc: MockMvc

    @Autowired
    protected lateinit var jdbcTemplate: NamedParameterJdbcTemplate

    @Autowired
    protected lateinit var tokenGenerator: TokenGenerator

    /** A valid access token of the user owning [email]. */
    protected fun token(email: String): String = tokenGenerator.generateAccessToken(userId(email), Email(email))

    protected fun userId(email: String): UserId =
        UserId(
            jdbcTemplate.queryForObject(
                """
                SELECT u.id FROM users u
                JOIN personal_informations pi ON pi.id = u.personal_information_id
                WHERE pi.email = :email
                """,
                MapSqlParameterSource("email", email),
                Long::class.java,
            )!!,
        )

    protected fun count(
        sql: String,
        params: Map<String, Any> = emptyMap(),
    ): Int = jdbcTemplate.queryForObject(sql, MapSqlParameterSource(params), Int::class.java)!!

    protected companion object {
        const val ROMAIN = "romain@example.com"
        const val TEST = "test@example.com"
        const val JOHN = "john.doe@example.com"
    }
}
