package defalt.policy

import defalt.exception.AccountErrorCode
import defalt.fixtures.AccountFixtures.JOHN_EMAIL
import defalt.fixtures.AccountFixtures.ROMAIN_EMAIL
import defalt.fixtures.AccountFixtures.TEST_EMAIL
import defalt.fixtures.AccountFixtures.newOwner
import defalt.fixtures.assertFailsWithCode
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import kotlin.test.assertEquals

class AccountOwnershipPolicyTest {
    @Test
    fun `accepts a single owner who is the creator with full ownership`() {
        AccountOwnershipPolicy.check(listOf(newOwner(ROMAIN_EMAIL, "100")), ROMAIN_EMAIL)
    }

    @Test
    fun `accepts shared ownership adding up to 100 with decimals`() {
        AccountOwnershipPolicy.check(
            listOf(newOwner(ROMAIN_EMAIL, "33.34"), newOwner(TEST_EMAIL, "33.33"), newOwner(JOHN_EMAIL, "33.33")),
            ROMAIN_EMAIL,
        )
    }

    @Test
    fun `accepts a total of 100 written with a different scale`() {
        AccountOwnershipPolicy.check(listOf(newOwner(ROMAIN_EMAIL, "100.00")), ROMAIN_EMAIL)
    }

    @Test
    fun `rejects an empty owners list`() {
        assertFailsWithCode(AccountErrorCode.OWNERS_REQUIRED) {
            AccountOwnershipPolicy.check(emptyList(), ROMAIN_EMAIL)
        }
    }

    @Test
    fun `rejects duplicate emails and reports them`() {
        val exception =
            assertFailsWithCode(AccountErrorCode.DUPLICATE_OWNER) {
                AccountOwnershipPolicy.check(
                    listOf(newOwner(ROMAIN_EMAIL, "50"), newOwner(ROMAIN_EMAIL, "50")),
                    ROMAIN_EMAIL,
                )
            }

        assertEquals(listOf(ROMAIN_EMAIL.value), exception.params["emails"])
    }

    @Test
    fun `rejects owners that do not include the creator`() {
        assertFailsWithCode(AccountErrorCode.CREATOR_NOT_OWNER) {
            AccountOwnershipPolicy.check(listOf(newOwner(TEST_EMAIL, "100")), ROMAIN_EMAIL)
        }
    }

    @Test
    fun `rejects a zero percentage`() {
        val exception =
            assertFailsWithCode(AccountErrorCode.INVALID_OWNERSHIP_PERCENTAGE) {
                AccountOwnershipPolicy.check(
                    listOf(newOwner(ROMAIN_EMAIL, "100"), newOwner(TEST_EMAIL, "0")),
                    ROMAIN_EMAIL,
                )
            }

        assertEquals(listOf(TEST_EMAIL.value), exception.params["emails"])
    }

    @Test
    fun `rejects a negative percentage`() {
        assertFailsWithCode(AccountErrorCode.INVALID_OWNERSHIP_PERCENTAGE) {
            AccountOwnershipPolicy.check(
                listOf(newOwner(ROMAIN_EMAIL, "110"), newOwner(TEST_EMAIL, "-10")),
                ROMAIN_EMAIL,
            )
        }
    }

    @Test
    fun `rejects a percentage above 100`() {
        assertFailsWithCode(AccountErrorCode.INVALID_OWNERSHIP_PERCENTAGE) {
            AccountOwnershipPolicy.check(listOf(newOwner(ROMAIN_EMAIL, "100.01")), ROMAIN_EMAIL)
        }
    }

    @Test
    fun `rejects a total below 100 and reports it`() {
        val exception =
            assertFailsWithCode(AccountErrorCode.OWNERSHIP_TOTAL_MISMATCH) {
                AccountOwnershipPolicy.check(
                    listOf(newOwner(ROMAIN_EMAIL, "60"), newOwner(TEST_EMAIL, "30")),
                    ROMAIN_EMAIL,
                )
            }

        assertEquals(BigDecimal("90"), exception.params["total"])
    }

    @Test
    fun `rejects a total above 100`() {
        assertFailsWithCode(AccountErrorCode.OWNERSHIP_TOTAL_MISMATCH) {
            AccountOwnershipPolicy.check(
                listOf(newOwner(ROMAIN_EMAIL, "60"), newOwner(TEST_EMAIL, "60")),
                ROMAIN_EMAIL,
            )
        }
    }

    @Test
    fun `checks duplicates before the creator membership`() {
        assertFailsWithCode(AccountErrorCode.DUPLICATE_OWNER) {
            AccountOwnershipPolicy.check(
                listOf(newOwner(TEST_EMAIL, "50"), newOwner(TEST_EMAIL, "50")),
                ROMAIN_EMAIL,
            )
        }
    }
}
