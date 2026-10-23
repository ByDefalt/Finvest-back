package defalt.usecase

import defalt.command.UpdateAccountCommand
import defalt.exception.AccountErrorCode
import defalt.fakes.AccountTestContext
import defalt.fakes.ROMAIN
import defalt.fixtures.AccountFixtures.COMPTE_COURANT
import defalt.fixtures.AccountFixtures.ROMAIN_ID
import defalt.fixtures.AccountFixtures.TEST_EMAIL
import defalt.fixtures.AccountFixtures.TEST_ID
import defalt.fixtures.AccountFixtures.account
import defalt.fixtures.AccountFixtures.compteCourant
import defalt.fixtures.AccountFixtures.newOwner
import defalt.fixtures.AccountFixtures.pea
import defalt.fixtures.assertFailsWithCode
import defalt.models.Pea
import defalt.valueobject.AccountId
import defalt.valueobject.Email
import defalt.valueobject.OwnerRef
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UpdateAccountUseCaseTest {
    private val context = AccountTestContext()
    private val accountId = AccountId(1L)

    @Test
    fun `updates the account, its details and replaces its owners`() {
        context.seedPea(1L)

        context.updateAccount(
            ROMAIN,
            UpdateAccountCommand(
                account = account(id = 1L, name = "Renamed", balance = "30000.00"),
                details = pea(depositLimit = "200000"),
                owners = listOf(newOwner(percentage = "50"), newOwner(TEST_EMAIL, "50")),
            ),
        )

        val updated = context.accounts.accounts.getValue(accountId)
        assertEquals("Renamed", updated.name)
        assertEquals(BigDecimal("30000.00"), updated.balance)
        assertEquals(BigDecimal("200000"), (context.peaDetails.details.getValue(accountId) as Pea).depositLimit)
        assertEquals(
            listOf(OwnerRef.Internal(ROMAIN_ID) to BigDecimal("50"), OwnerRef.Internal(TEST_ID) to BigDecimal("50")),
            context.owners.ownersOf(accountId).map { it.owner to it.ownershipPercentage },
        )
    }

    @Test
    fun `fails when the account does not exist`() {
        assertFailsWithCode(AccountErrorCode.ACCOUNT_NOT_FOUND) {
            context.updateAccount(ROMAIN, UpdateAccountCommand(account(id = 1L), pea(), listOf(newOwner())))
        }

        assertTrue(context.accounts.updated.isEmpty())
    }

    @Test
    fun `denies the update to a user who does not own the account`() {
        context.seedPea(1L, owner = OwnerRef.Internal(TEST_ID))

        assertFailsWithCode(AccountErrorCode.ACCOUNT_ACCESS_DENIED) {
            context.updateAccount(ROMAIN, UpdateAccountCommand(account(id = 1L), pea(), listOf(newOwner())))
        }

        assertTrue(context.accounts.updated.isEmpty())
        assertEquals(listOf(OwnerRef.Internal(TEST_ID)), context.owners.ownersOf(accountId).map { it.owner })
    }

    @Test
    fun `refuses to change the account type`() {
        context.seedPea(1L)

        assertFailsWithCode(AccountErrorCode.ACCOUNT_TYPE_IMMUTABLE) {
            context.updateAccount(
                ROMAIN,
                UpdateAccountCommand(account(id = 1L, typeCode = COMPTE_COURANT), compteCourant(), listOf(newOwner())),
            )
        }

        assertTrue(context.accounts.updated.isEmpty())
        assertTrue(context.compteCourantDetails.details.isEmpty())
    }

    @Test
    fun `rejects an invalid ownership before opening any transaction`() {
        context.seedPea(1L)

        assertFailsWithCode(AccountErrorCode.OWNERSHIP_TOTAL_MISMATCH) {
            context.updateAccount(ROMAIN, UpdateAccountCommand(account(id = 1L), pea(), listOf(newOwner(percentage = "80"))))
        }

        assertEquals(0, context.transactions.executions)
    }

    @Test
    fun `keeps the current owners when a new owner cannot be resolved`() {
        context.seedPea(1L)

        assertFailsWithCode(AccountErrorCode.OWNER_NOT_FOUND) {
            context.updateAccount(
                ROMAIN,
                UpdateAccountCommand(
                    account(id = 1L, name = "Renamed"),
                    pea(),
                    listOf(newOwner(percentage = "50"), newOwner(Email("ghost@example.com"), "50")),
                ),
            )
        }

        assertTrue(context.accounts.updated.isEmpty())
        assertEquals(listOf(OwnerRef.Internal(ROMAIN_ID)), context.owners.ownersOf(accountId).map { it.owner })
    }
}
