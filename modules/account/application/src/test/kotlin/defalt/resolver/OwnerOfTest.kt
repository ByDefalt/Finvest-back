package defalt.resolver

import defalt.exception.AccountErrorCode
import defalt.fixtures.AccountFixtures.JOHN_EMAIL
import defalt.fixtures.AccountFixtures.ROMAIN_EMAIL
import defalt.fixtures.AccountFixtures.ROMAIN_ID
import defalt.fixtures.assertFailsWithCode
import defalt.valueobject.OwnerRef
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class OwnerOfTest {
    private val resolved = mapOf(ROMAIN_EMAIL to OwnerRef.Internal(ROMAIN_ID))

    @Test
    fun `returns the owner resolved for the email`() {
        assertEquals(OwnerRef.Internal(ROMAIN_ID), resolved.ownerOf(ROMAIN_EMAIL))
    }

    @Test
    fun `fails with a translatable code when the email was not resolved`() {
        val exception = assertFailsWithCode(AccountErrorCode.OWNER_NOT_FOUND) { resolved.ownerOf(JOHN_EMAIL) }

        assertEquals(listOf(JOHN_EMAIL.value), exception.params["emails"])
    }
}
