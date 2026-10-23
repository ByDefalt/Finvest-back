package defalt.persistence.repository

import defalt.exception.CommonErrorCode
import defalt.fixtures.AccountFixtures.ROMAIN_EMAIL
import defalt.fixtures.AccountFixtures.account
import defalt.fixtures.AccountFixtures.owner
import defalt.fixtures.assertFailsWithCode
import defalt.valueobject.OwnerRef
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AccountOwnerJdbcRepositoryTest : AccountJdbcIntegrationTest() {
    @Test
    fun `creates internal and external owners and reads them back`() {
        val accountId = accountRepository.createAccount(account())

        val ids =
            accountOwnerRepository.createAccountOwners(
                listOf(
                    owner(accountId.value, OwnerRef.Internal(romainId), "60.00"),
                    owner(accountId.value, OwnerRef.External(johnExternalId), "40.00"),
                ),
            )

        val owners = accountOwnerRepository.getAccountOwnersByAccountIds(setOf(accountId))
        assertEquals(ids.toSet(), owners.map { it.id }.toSet())
        assertEquals(
            setOf(OwnerRef.Internal(romainId) to BigDecimal("60.00"), OwnerRef.External(johnExternalId) to BigDecimal("40.00")),
            owners.map { it.owner to it.ownershipPercentage }.toSet(),
        )
        assertTrue(owners.all { it.accountId == accountId })
    }

    @Test
    fun `getAccountOwnersByAccountIds returns the owners of the requested accounts only`() {
        val first = persistAccount()
        val second = persistAccount(owner = OwnerRef.Internal(testUserId))
        persistAccount()

        val owners = accountOwnerRepository.getAccountOwnersByAccountIds(setOf(first, second))

        assertEquals(setOf(first, second), owners.map { it.accountId }.toSet())
    }

    @Test
    fun `getAccountOwnersByAccountIds returns an empty list for no id`() {
        assertTrue(accountOwnerRepository.getAccountOwnersByAccountIds(emptySet()).isEmpty())
    }

    @Test
    fun `refuses to persist an unresolved owner`() {
        val accountId = accountRepository.createAccount(account())

        assertFailsWithCode(CommonErrorCode.INTERNAL_ERROR) {
            accountOwnerRepository.createAccountOwners(listOf(owner(accountId.value, OwnerRef.Unknown(ROMAIN_EMAIL))))
        }
    }

    @Test
    fun `deleteAccountOwnersByAccountId removes the owners of that account only`() {
        val target = persistAccount()
        val other = persistAccount()

        accountOwnerRepository.deleteAccountOwnersByAccountId(target)

        assertEquals(0, count("account_owners", target))
        assertEquals(1, count("account_owners", other))
    }
}
