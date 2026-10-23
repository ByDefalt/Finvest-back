package defalt.persistence.repository

import defalt.persistence.PersistenceTestApplication
import defalt.persistence.RealDatabaseTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jdbc.test.autoconfigure.DataJdbcTest
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.test.context.ContextConfiguration

/**
 * Base class of the JDBC repository tests: a Spring JDBC slice on a real database (see [RealDatabaseTest]).
 * Each test runs in a transaction rolled back at its end. Subclasses import the repositories they test.
 */
@DataJdbcTest
@RealDatabaseTest
@ContextConfiguration(classes = [PersistenceTestApplication::class])
abstract class JdbcRepositoryTest {
    /** Template to prepare and check the data of the tests. */
    @Autowired
    protected lateinit var jdbcTemplate: NamedParameterJdbcTemplate
}
