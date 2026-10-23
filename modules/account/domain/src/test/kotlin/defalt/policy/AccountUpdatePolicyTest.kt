package defalt.policy

import defalt.exception.AccountErrorCode
import defalt.fixtures.AccountFixtures.COMPTE_COURANT
import defalt.fixtures.AccountFixtures.PEA
import defalt.fixtures.AccountFixtures.account
import defalt.fixtures.assertFailsWithCode
import org.junit.jupiter.api.Test

class AccountUpdatePolicyTest {
    @Test
    fun `allows changing every field except the account type`() {
        AccountUpdatePolicy.check(
            existing = account(id = 1L, typeCode = PEA),
            updated = account(id = 1L, typeCode = PEA, name = "Renamed", balance = "1.00", bankBic = "CMBRFR2B"),
        )
    }

    @Test
    fun `rejects a change of account type`() {
        assertFailsWithCode(AccountErrorCode.ACCOUNT_TYPE_IMMUTABLE) {
            AccountUpdatePolicy.check(
                existing = account(id = 1L, typeCode = PEA),
                updated = account(id = 1L, typeCode = COMPTE_COURANT),
            )
        }
    }
}
