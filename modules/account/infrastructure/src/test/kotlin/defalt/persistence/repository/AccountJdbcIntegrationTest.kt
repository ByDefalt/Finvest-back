package defalt.persistence.repository

import defalt.fixtures.AccountFixtures
import defalt.logger.Logger
import defalt.logger.NullLogger
import defalt.models.Account
import defalt.valueobject.AccountId
import defalt.valueobject.Email
import defalt.valueobject.ExternalUserId
import defalt.valueobject.OwnerRef
import defalt.valueobject.UserId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.test.context.jdbc.Sql

/**
 * Base class of the account JDBC repository tests, run against MariaDB (Testcontainers).
 * Every test class shares the same Spring context, hence the same container, and each test
 * runs in a transaction rolled back at the end.
 */
@Import(
    AccountJdbcIntegrationTest.LoggerConfiguration::class,
    AccountJdbcRepository::class,
    AccountOwnerJdbcRepository::class,
    CompteCourantJdbcRepository::class,
    LivretJdbcRepository::class,
    PeaJdbcRepository::class,
    CompteTitresJdbcRepository::class,
    AssuranceVieJdbcRepository::class,
    PerJdbcRepository::class,
    PeeJdbcRepository::class,
)
@Sql("/db/test/reference_data.sql")
abstract class AccountJdbcIntegrationTest : JdbcRepositoryTest() {
    @Autowired
    protected lateinit var accountRepository: AccountJdbcRepository

    @Autowired
    protected lateinit var accountOwnerRepository: AccountOwnerJdbcRepository

    protected val romainId: UserId by lazy { userId(AccountFixtures.ROMAIN_EMAIL) }
    protected val testUserId: UserId by lazy { userId(AccountFixtures.TEST_EMAIL) }
    protected val johnExternalId: ExternalUserId by lazy { externalUserId(AccountFixtures.JOHN_EMAIL) }

    /** Persists [account] fully owned by [owner] (Romain by default) and returns its generated id. */
    protected fun persistAccount(
        account: Account = AccountFixtures.account(),
        owner: OwnerRef = OwnerRef.Internal(romainId),
    ): AccountId {
        val accountId = accountRepository.createAccount(account)
        accountOwnerRepository.createAccountOwners(listOf(AccountFixtures.owner(accountId.value, owner)))
        return accountId
    }

    protected fun count(
        table: String,
        accountId: AccountId,
    ): Int =
        jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM $table WHERE account_id = :accountId",
            MapSqlParameterSource("accountId", accountId.value),
            Int::class.java,
        )!!

    private fun userId(email: Email): UserId =
        UserId(
            jdbcTemplate.queryForObject(
                """
                SELECT u.id FROM users u
                JOIN personal_informations pi ON pi.id = u.personal_information_id
                WHERE pi.email = :email
                """,
                MapSqlParameterSource("email", email.value),
                Long::class.java,
            )!!,
        )

    private fun externalUserId(email: Email): ExternalUserId =
        ExternalUserId(
            jdbcTemplate.queryForObject(
                """
                SELECT u.id FROM users_external u
                JOIN personal_informations pi ON pi.id = u.personal_information_id
                WHERE pi.email = :email
                """,
                MapSqlParameterSource("email", email.value),
                Long::class.java,
            )!!,
        )

    @TestConfiguration(proxyBeanMethods = false)
    class LoggerConfiguration {
        @Bean
        fun logger(): Logger = NullLogger()
    }
}
