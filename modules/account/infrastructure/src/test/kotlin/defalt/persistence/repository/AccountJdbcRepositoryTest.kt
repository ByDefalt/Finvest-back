package defalt.persistence.repository

import defalt.fixtures.AccountFixtures.account
import defalt.valueobject.AccountId
import defalt.valueobject.AccountStatusCode
import defalt.valueobject.AccountTypeCode
import defalt.valueobject.BankBic
import defalt.valueobject.OwnerRef
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AccountJdbcRepositoryTest : AccountJdbcIntegrationTest() {
    @Test
    fun `createAccount persists every field and returns the generated id`() {
        val accountId = accountRepository.createAccount(account(name = "Mon PEA", balance = "1234.5600"))

        val saved = assertNotNull(accountRepository.getAccountByAccountId(accountId))
        assertEquals(account(id = accountId.value, name = "Mon PEA", balance = "1234.5600"), saved)
    }

    @Test
    fun `createAccount generates a distinct id for each account`() {
        val first = accountRepository.createAccount(account())
        val second = accountRepository.createAccount(account())

        assertTrue(second.value > first.value)
    }

    @Test
    fun `getAccountByAccountId returns null for an unknown id`() {
        assertNull(accountRepository.getAccountByAccountId(AccountId(-1L)))
    }

    @Test
    fun `getAccountsByAccountIds returns only the requested accounts`() {
        val first = accountRepository.createAccount(account(name = "First"))
        accountRepository.createAccount(account(name = "Second"))
        val third = accountRepository.createAccount(account(name = "Third"))

        val names = accountRepository.getAccountsByAccountIds(setOf(first, third)).map { it.name }

        assertEquals(setOf("First", "Third"), names.toSet())
    }

    @Test
    fun `getAccountsByAccountIds returns an empty list for no id`() {
        assertTrue(accountRepository.getAccountsByAccountIds(emptySet()).isEmpty())
    }

    @Test
    fun `getAccountsByUserId returns the accounts owned by the user only`() {
        val romainAccount = persistAccount(account(name = "Romain"))
        persistAccount(account(name = "Test"), owner = OwnerRef.Internal(testUserId))

        assertEquals(listOf(romainAccount), accountRepository.getAccountsByUserId(romainId).map { it.id })
    }

    @Test
    fun `getAccountsWithBanksByUserId joins the bank of each account`() {
        val accountId = persistAccount(account(bankBic = "FTNOFRP1"))
        persistAccount(account(name = "Test"), owner = OwnerRef.Internal(testUserId))

        val result = accountRepository.getAccountsWithBanksByUserId(romainId).single()

        assertEquals(accountId, result.account.id)
        assertEquals(BankBic("FTNOFRP1"), result.bank.bic)
        assertEquals("Fortuneo", result.bank.name)
        assertEquals("fortuneo.png", result.bank.logo)
    }

    @Test
    fun `updateAccount changes the editable fields but keeps the type and creation date`() {
        val accountId = accountRepository.createAccount(account())
        val closedAt = LocalDateTime.of(2025, 6, 1, 12, 0)

        accountRepository.updateAccount(
            account(id = accountId.value, name = "Renamed", balance = "10.0000", bankBic = "CMBRFR2B", typeCode = "LIVRET")
                .copy(
                    accountStatusCode = AccountStatusCode("CLOSED"),
                    closedAt = closedAt,
                    description = null,
                    createdAt = LocalDateTime.of(2000, 1, 1, 0, 0),
                ),
        )

        val updated = assertNotNull(accountRepository.getAccountByAccountId(accountId))
        assertEquals("Renamed", updated.name)
        assertEquals(BigDecimal("10.0000"), updated.balance)
        assertEquals(BankBic("CMBRFR2B"), updated.bankBic)
        assertEquals(AccountStatusCode("CLOSED"), updated.accountStatusCode)
        assertEquals(closedAt, updated.closedAt)
        assertNull(updated.description)
        assertEquals(AccountTypeCode("PEA"), updated.accountTypeCode)
        assertEquals(account().createdAt, updated.createdAt)
    }

    @Test
    fun `every read maps the closing date of a closed account`() {
        val closedAt = LocalDateTime.of(2025, 6, 1, 12, 0)
        val accountId = persistAccount(account().copy(closedAt = closedAt, accountStatusCode = AccountStatusCode("CLOSED")))

        assertEquals(closedAt, accountRepository.getAccountByAccountId(accountId)?.closedAt)
        assertEquals(closedAt, accountRepository.getAccountsByAccountIds(setOf(accountId)).single().closedAt)
        assertEquals(closedAt, accountRepository.getAccountsByUserId(romainId).single().closedAt)
        assertEquals(
            closedAt,
            accountRepository
                .getAccountsWithBanksByUserId(romainId)
                .single()
                .account.closedAt,
        )
    }

    @Test
    fun `updateAccount does not touch the other accounts`() {
        val accountId = accountRepository.createAccount(account(name = "Target"))
        val otherId = accountRepository.createAccount(account(name = "Other"))

        accountRepository.updateAccount(account(id = accountId.value, name = "Renamed"))

        assertEquals("Other", accountRepository.getAccountByAccountId(otherId)?.name)
    }

    @Test
    fun `deleteAccount removes the account only`() {
        val accountId = accountRepository.createAccount(account())
        val otherId = accountRepository.createAccount(account())

        accountRepository.deleteAccount(accountId)

        assertNull(accountRepository.getAccountByAccountId(accountId))
        assertNotNull(accountRepository.getAccountByAccountId(otherId))
    }
}
