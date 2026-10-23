package defalt.models

import defalt.fixtures.AccountFixtures.JOHN_EXTERNAL_ID
import defalt.fixtures.AccountFixtures.newOwner
import defalt.valueobject.AccountId
import defalt.valueobject.OwnerRef
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import kotlin.test.assertEquals

class NewAccountOwnerTest {
    @Test
    fun `toAccountOwner attaches the owner to the account and keeps the percentage`() {
        val owner = newOwner(percentage = "37.5").toAccountOwner(AccountId(7L), OwnerRef.External(JOHN_EXTERNAL_ID))

        assertEquals(AccountId(7L), owner.accountId)
        assertEquals(OwnerRef.External(JOHN_EXTERNAL_ID), owner.owner)
        assertEquals(BigDecimal("37.5"), owner.ownershipPercentage)
    }
}
