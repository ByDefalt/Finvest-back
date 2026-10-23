package defalt.solver

import defalt.exception.AccountErrorCode
import defalt.fakes.InMemoryDetailsRepositorySolver
import defalt.fixtures.AccountFixtures.COMPTE_COURANT
import defalt.fixtures.AccountFixtures.PEA
import defalt.fixtures.AccountFixtures.account
import defalt.fixtures.AccountFixtures.compteCourant
import defalt.fixtures.AccountFixtures.pea
import defalt.fixtures.assertFailsWithCode
import defalt.valueobject.AccountId
import defalt.valueobject.AccountTypeCode
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DetailsRepositoryResolverTest {
    private val peaSolver = InMemoryDetailsRepositorySolver(PEA)
    private val compteCourantSolver = InMemoryDetailsRepositorySolver(COMPTE_COURANT)
    private val resolver = DetailsRepositoryResolver(listOf(peaSolver, compteCourantSolver))

    @Test
    fun `creates the details with the solver of the account type only`() {
        resolver.createAccountDetails(AccountId(1L), AccountTypeCode(PEA), pea())

        assertEquals(setOf(AccountId(1L)), peaSolver.details.keys)
        assertTrue(compteCourantSolver.details.isEmpty())
    }

    @Test
    fun `updates the details with the solver of the account type`() {
        compteCourantSolver.seed(compteCourant(accountId = 1L))
        val updated = compteCourant(accountId = 1L).copy(holderName = "New holder")

        resolver.updateAccountDetails(AccountId(1L), AccountTypeCode(COMPTE_COURANT), updated)

        assertEquals(updated, compteCourantSolver.details.getValue(AccountId(1L)))
    }

    @Test
    fun `deletes the details with the solver of the account type`() {
        peaSolver.seed(pea(accountId = 1L))
        compteCourantSolver.seed(compteCourant(accountId = 1L))

        resolver.deleteAccountDetails(AccountId(1L), AccountTypeCode(PEA))

        assertTrue(peaSolver.details.isEmpty())
        assertEquals(setOf(AccountId(1L)), compteCourantSolver.details.keys)
    }

    @Test
    fun `aggregates the details of accounts of different types`() {
        peaSolver.seed(pea(accountId = 1L))
        compteCourantSolver.seed(compteCourant(accountId = 2L))

        val details =
            resolver.getAccountsDetailsByAccounts(
                listOf(account(id = 1L, typeCode = PEA), account(id = 2L, typeCode = COMPTE_COURANT)),
            )

        assertEquals(setOf(pea(accountId = 1L), compteCourant(accountId = 2L)), details.toSet())
    }

    @Test
    fun `fails for an account type without solver`() {
        val unknown = AccountTypeCode("UNKNOWN")

        assertFailsWithCode(AccountErrorCode.INVALID_ACCOUNT_TYPE) { resolver.createAccountDetails(AccountId(1L), unknown, pea()) }
        assertFailsWithCode(AccountErrorCode.INVALID_ACCOUNT_TYPE) { resolver.updateAccountDetails(AccountId(1L), unknown, pea()) }
        assertFailsWithCode(AccountErrorCode.INVALID_ACCOUNT_TYPE) { resolver.deleteAccountDetails(AccountId(1L), unknown) }
    }
}
