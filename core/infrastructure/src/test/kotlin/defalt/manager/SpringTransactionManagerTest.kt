package defalt.manager

import defalt.exception.CommonErrorCode
import defalt.exception.CustomException
import defalt.persistence.repository.JdbcRepositoryTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.dao.DuplicateKeyException
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertSame
import kotlin.test.assertTrue

// The slice is transactional by default: disable it so that the manager owns the transactions under test
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Import(SpringTransactionManager::class)
class SpringTransactionManagerTest : JdbcRepositoryTest() {
    @Autowired
    private lateinit var transactionManager: TransactionManager

    @AfterEach
    fun cleanUp() {
        jdbcTemplate.update("DELETE FROM currencies", MapSqlParameterSource())
    }

    @Test
    fun `execute commits the block and returns its result`() {
        val result =
            transactionManager.execute {
                insertCurrency("EUR")
                "done"
            }

        assertEquals("done", result.getOrThrow())
        assertEquals(1, countCurrencies())
    }

    @Test
    fun `execute rolls back every write when the block fails`() {
        val result =
            transactionManager.execute {
                insertCurrency("EUR")
                error("boom")
            }

        assertIs<IllegalStateException>(result.exceptionOrNull())
        assertEquals(0, countCurrencies())
    }

    @Test
    fun `execute rolls back the first write and reports a duplicate value`() {
        val result =
            transactionManager.execute {
                insertCurrency("USD")
                insertCurrency("EUR")
                insertCurrency("EUR")
            }

        val exception = assertIs<CustomException>(result.exceptionOrNull())
        assertEquals(CommonErrorCode.DUPLICATE, exception.code)
        assertIs<DuplicateKeyException>(exception.cause)
        assertEquals(0, countCurrencies())
    }

    @Test
    fun `execute reports the violation of a foreign key`() {
        val result =
            transactionManager.execute {
                jdbcTemplate.update(
                    """
                INSERT INTO accounts (bank_bic, name, balance, currency_code, account_status_code, account_type_code)
                VALUES ('UNKNOWN', 'Orphan', 0, 'EUR', 'ACTIVE', 'PEA')
                """,
                    MapSqlParameterSource(),
                )
            }

        val exception = assertIs<CustomException>(result.exceptionOrNull())
        assertEquals(CommonErrorCode.INTEGRITY_VIOLATION, exception.code)
    }

    @Test
    fun `execute lets the custom exceptions through`() {
        val error = CustomException(CommonErrorCode.NOT_FOUND, "missing")

        assertSame(error, transactionManager.execute { throw error }.exceptionOrNull())
    }

    @Test
    fun `executeReadOnly forbids writes`() {
        val result = transactionManager.executeReadOnly { insertCurrency("EUR") }

        assertTrue(result.isFailure)
        assertEquals(0, countCurrencies())
    }

    @Test
    fun `executeReadOnly returns the block result`() {
        insertCurrency("USD")

        val result = transactionManager.executeReadOnly { countCurrencies() }

        assertEquals(1, result.getOrThrow())
    }

    private fun insertCurrency(code: String) {
        jdbcTemplate.update("INSERT INTO currencies (code) VALUES (:code)", MapSqlParameterSource("code", code))
    }

    private fun countCurrencies(): Int =
        jdbcTemplate.queryForObject("SELECT COUNT(*) FROM currencies", MapSqlParameterSource(), Int::class.java)!!
}
