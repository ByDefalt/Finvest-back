package defalt.persistence

import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles

/**
 * Runs a Spring test against a real database (see [TestDatabaseConfiguration]) instead of an embedded one,
 * with the `test` profile.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestDatabaseConfiguration::class)
@ActiveProfiles("test")
annotation class RealDatabaseTest
