package defalt.usecase

import defalt.exception.AccountErrorCode
import defalt.fakes.AccountTestContext
import defalt.fakes.ROMAIN
import defalt.fixtures.AccountFixtures.JOHN_EMAIL
import defalt.fixtures.AccountFixtures.JOHN_EXTERNAL_ID
import defalt.fixtures.AccountFixtures.ROMAIN_EMAIL
import defalt.fixtures.AccountFixtures.TEST_ID
import defalt.fixtures.AccountFixtures.account
import defalt.fixtures.AccountFixtures.owner
import defalt.fixtures.AccountFixtures.pea
import defalt.fixtures.assertFailsWithCode
import defalt.valueobject.AccountId
import defalt.valueobject.ExternalUserId
import defalt.valueobject.OwnerRef
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetAccountWithDetailsUseCaseTest {
    private val context = AccountTestContext()

    @Test
    fun `returns the accounts of the user with their bank, details and owner identities`() {
        context.seedPea(1L)
        context.owners.seed(owner(accountId = 1L, owner = OwnerRef.External(JOHN_EXTERNAL_ID), percentage = "0"))
        context.seedPea(2L, owner = OwnerRef.Internal(TEST_ID))

        val result = context.getAccountsWithDetails(ROMAIN)

        val accountWithDetails = result.single()
        assertEquals(AccountId(1L), accountWithDetails.account.account.id)
        assertEquals("FTNOFRP1", accountWithDetails.account.bank.bic.value)
        assertEquals(pea(accountId = 1L), accountWithDetails.details)
        assertEquals(
            listOf(ROMAIN_EMAIL to "Romain", JOHN_EMAIL to "John"),
            accountWithDetails.owners.map { it.identity.email to it.identity.firstName },
        )
    }

    @Test
    fun `returns an empty list when the user owns no account`() {
        context.seedPea(1L, owner = OwnerRef.Internal(TEST_ID))

        assertTrue(context.getAccountsWithDetails(ROMAIN).isEmpty())
    }

    @Test
    fun `fails when an account has no details`() {
        context.accounts.seed(account(id = 1L))
        context.owners.seed(owner(accountId = 1L))

        assertFailsWithCode(AccountErrorCode.ACCOUNT_DETAILS_NOT_FOUND) { context.getAccountsWithDetails(ROMAIN) }
    }

    @Test
    fun `fails when an owner was never resolved`() {
        context.seedPea(1L)
        context.owners.seed(owner(accountId = 1L, owner = OwnerRef.Unknown(JOHN_EMAIL), percentage = "0"))

        assertFailsWithCode(AccountErrorCode.OWNER_IDENTITY_NOT_FOUND) { context.getAccountsWithDetails(ROMAIN) }
    }

    @Test
    fun `fails when an owner identity cannot be found`() {
        context.seedPea(1L)
        context.owners.seed(owner(accountId = 1L, owner = OwnerRef.External(ExternalUserId(999L)), percentage = "0"))

        assertFailsWithCode(AccountErrorCode.OWNER_IDENTITY_NOT_FOUND) { context.getAccountsWithDetails(ROMAIN) }
    }
}
