package defalt.usecase

import defalt.command.CreateAccountCommand
import defalt.exception.AccountErrorCode
import defalt.fakes.AccountTestContext
import defalt.fakes.ROMAIN
import defalt.fixtures.AccountFixtures.JOHN_EMAIL
import defalt.fixtures.AccountFixtures.JOHN_EXTERNAL_ID
import defalt.fixtures.AccountFixtures.ROMAIN_ID
import defalt.fixtures.AccountFixtures.TEST_EMAIL
import defalt.fixtures.AccountFixtures.account
import defalt.fixtures.AccountFixtures.newOwner
import defalt.fixtures.AccountFixtures.pea
import defalt.fixtures.assertFailsWithCode
import defalt.models.Pea
import defalt.valueobject.Email
import defalt.valueobject.OwnerRef
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CreateAccountUseCaseTest {
    private val context = AccountTestContext()

    @Test
    fun `creates the account, its details and its resolved owners`() {
        context.createAccount(
            ROMAIN,
            CreateAccountCommand(
                account = account(name = "Mon PEA"),
                details = pea(depositLimit = "150000"),
                owners = listOf(newOwner(percentage = "60"), newOwner(JOHN_EMAIL, "40")),
            ),
        )

        val created =
            context.accounts.accounts.values
                .single()
        assertEquals("Mon PEA", created.name)
        assertEquals(BigDecimal("150000"), (context.peaDetails.details.getValue(created.id) as Pea).depositLimit)
        assertEquals(
            listOf(OwnerRef.Internal(ROMAIN_ID) to BigDecimal("60"), OwnerRef.External(JOHN_EXTERNAL_ID) to BigDecimal("40")),
            context.owners.ownersOf(created.id).map { it.owner to it.ownershipPercentage },
        )
    }

    @Test
    fun `runs all the writes in a single transaction`() {
        context.createAccount(ROMAIN, CreateAccountCommand(account(), pea(), listOf(newOwner())))

        assertEquals(1, context.transactions.executions)
    }

    @Test
    fun `rejects an invalid ownership before opening any transaction`() {
        assertFailsWithCode(AccountErrorCode.CREATOR_NOT_OWNER) {
            context.createAccount(ROMAIN, CreateAccountCommand(account(), pea(), listOf(newOwner(TEST_EMAIL))))
        }

        assertEquals(0, context.transactions.executions)
        assertTrue(context.accounts.accounts.isEmpty())
    }

    @Test
    fun `rejects an unknown owner email without writing anything`() {
        val exception =
            assertFailsWithCode(AccountErrorCode.OWNER_NOT_FOUND) {
                context.createAccount(
                    ROMAIN,
                    CreateAccountCommand(
                        account(),
                        pea(),
                        listOf(newOwner(percentage = "50"), newOwner(Email("ghost@example.com"), "50")),
                    ),
                )
            }

        assertEquals(listOf("ghost@example.com"), exception.params["emails"])
        assertTrue(context.accounts.accounts.isEmpty())
        assertTrue(context.owners.owners.isEmpty())
    }

    @Test
    fun `rejects an account type without details repository`() {
        assertFailsWithCode(AccountErrorCode.INVALID_ACCOUNT_TYPE) {
            context.createAccount(ROMAIN, CreateAccountCommand(account(typeCode = "UNKNOWN"), pea(), listOf(newOwner())))
        }
    }
}
