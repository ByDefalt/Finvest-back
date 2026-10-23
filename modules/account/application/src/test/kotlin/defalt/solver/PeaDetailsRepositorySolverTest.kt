package defalt.solver

import defalt.fixtures.AccountFixtures.COMPTE_COURANT
import defalt.fixtures.AccountFixtures.PEA
import defalt.fixtures.AccountFixtures.account
import defalt.fixtures.AccountFixtures.pea
import defalt.models.Pea
import defalt.repository.PeaRepository
import defalt.valueobject.AccountId
import defalt.valueobject.AccountTypeCode
import defalt.valueobject.UserId
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PeaDetailsRepositorySolverTest {
    private val repository = InMemoryPeaRepository()
    private val solver = PeaDetailsRepositorySolver(repository)

    @Test
    fun `supports only the PEA account type`() {
        assertTrue(solver.supports(AccountTypeCode(PEA)))
        assertFalse(solver.supports(AccountTypeCode(COMPTE_COURANT)))
    }

    @Test
    fun `creates the details for the given account id`() {
        solver.createAccountDetails(AccountId(7L), pea(accountId = 0L))

        assertEquals(pea(accountId = 7L), repository.peas.getValue(AccountId(7L)))
    }

    @Test
    fun `updates the details of the given account id, ignoring the id carried by the details`() {
        repository.peas[AccountId(7L)] = pea(accountId = 7L)

        solver.updateAccountDetails(AccountId(7L), pea(accountId = 99L, depositLimit = "1000"))

        assertEquals(BigDecimal("1000"), repository.peas.getValue(AccountId(7L)).depositLimit)
        assertFalse(AccountId(99L) in repository.peas)
    }

    @Test
    fun `deletes the details of the given account id`() {
        repository.peas[AccountId(7L)] = pea(accountId = 7L)

        solver.deleteAccountDetails(AccountId(7L))

        assertTrue(repository.peas.isEmpty())
    }

    @Test
    fun `only queries the PEA accounts`() {
        repository.peas[AccountId(1L)] = pea(accountId = 1L)

        val details =
            solver.getAccountsDetailsByAccounts(
                listOf(account(id = 1L, typeCode = PEA), account(id = 2L, typeCode = COMPTE_COURANT)),
            )

        assertEquals(listOf(pea(accountId = 1L)), details)
        assertEquals(listOf(setOf(AccountId(1L))), repository.queriedIds)
    }

    private class InMemoryPeaRepository : PeaRepository {
        val peas = linkedMapOf<AccountId, Pea>()
        val queriedIds = mutableListOf<Set<AccountId>>()

        override fun getPeasByUserId(userId: UserId): List<Pea> = error("not used")

        override fun getPeasByAccountIds(accountIds: Set<AccountId>): List<Pea> {
            queriedIds += accountIds
            return accountIds.mapNotNull { peas[it] }
        }

        override fun getPeaByAccountId(accountId: AccountId): Pea? = peas[accountId]

        override fun createPea(pea: Pea) {
            peas[pea.accountId] = pea
        }

        override fun updatePea(pea: Pea) {
            peas[pea.accountId] = pea
        }

        override fun deletePea(accountId: AccountId) {
            peas.remove(accountId)
        }
    }
}
