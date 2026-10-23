package defalt.usecase

import defalt.exception.AccountErrorCode
import defalt.fakes.AccountTestContext
import defalt.fakes.ROMAIN
import defalt.fixtures.AccountFixtures.TEST_ID
import defalt.fixtures.assertFailsWithCode
import defalt.valueobject.AccountId
import defalt.valueobject.OwnerRef
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DeleteAccountUseCaseTest {
    private val context = AccountTestContext()

    @Test
    fun `deletes the owners, the details and the account`() {
        context.seedPea(1L)
        context.seedPea(2L)

        context.deleteAccount(ROMAIN, AccountId(1L))

        assertEquals(setOf(AccountId(2L)), context.accounts.accounts.keys)
        assertEquals(setOf(AccountId(2L)), context.peaDetails.details.keys)
        assertEquals(
            setOf(AccountId(2L)),
            context.owners.owners
                .map { it.accountId }
                .toSet(),
        )
        assertEquals(1, context.transactions.executions)
    }

    @Test
    fun `fails when the account does not exist`() {
        assertFailsWithCode(AccountErrorCode.ACCOUNT_NOT_FOUND) {
            context.deleteAccount(ROMAIN, AccountId(1L))
        }
    }

    @Test
    fun `denies the deletion to a user who does not own the account`() {
        context.seedPea(1L, owner = OwnerRef.Internal(TEST_ID))

        assertFailsWithCode(AccountErrorCode.ACCOUNT_ACCESS_DENIED) {
            context.deleteAccount(ROMAIN, AccountId(1L))
        }

        assertTrue(AccountId(1L) in context.accounts.accounts)
        assertTrue(AccountId(1L) in context.peaDetails.details)
        assertFalse(context.owners.ownersOf(AccountId(1L)).isEmpty())
    }
}
