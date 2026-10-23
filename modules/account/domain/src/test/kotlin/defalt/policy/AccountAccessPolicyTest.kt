package defalt.policy

import defalt.exception.AccountErrorCode
import defalt.fixtures.AccountFixtures.JOHN_EXTERNAL_ID
import defalt.fixtures.AccountFixtures.ROMAIN_EMAIL
import defalt.fixtures.AccountFixtures.ROMAIN_ID
import defalt.fixtures.AccountFixtures.TEST_ID
import defalt.fixtures.AccountFixtures.owner
import defalt.fixtures.assertFailsWithCode
import defalt.valueobject.AccountId
import defalt.valueobject.OwnerRef
import defalt.valueobject.UserId
import org.junit.jupiter.api.Test

class AccountAccessPolicyTest {
    private val accountId = AccountId(42L)

    @Test
    fun `allows an internal owner of the account`() {
        AccountAccessPolicy.checkIsOwner(
            accountId,
            listOf(owner(42L, OwnerRef.Internal(TEST_ID), "50"), owner(42L, OwnerRef.Internal(ROMAIN_ID), "50")),
            ROMAIN_ID,
        )
    }

    @Test
    fun `denies a user who is not among the owners`() {
        assertFailsWithCode(AccountErrorCode.ACCOUNT_ACCESS_DENIED) {
            AccountAccessPolicy.checkIsOwner(accountId, listOf(owner(42L, OwnerRef.Internal(TEST_ID))), ROMAIN_ID)
        }
    }

    @Test
    fun `denies access when the account has no owner`() {
        assertFailsWithCode(AccountErrorCode.ACCOUNT_ACCESS_DENIED) {
            AccountAccessPolicy.checkIsOwner(accountId, emptyList(), ROMAIN_ID)
        }
    }

    @Test
    fun `does not confuse an external owner with an internal user having the same id`() {
        assertFailsWithCode(AccountErrorCode.ACCOUNT_ACCESS_DENIED) {
            AccountAccessPolicy.checkIsOwner(
                accountId,
                listOf(owner(42L, OwnerRef.External(JOHN_EXTERNAL_ID))),
                UserId(JOHN_EXTERNAL_ID.value),
            )
        }
    }

    @Test
    fun `does not grant access through an unresolved owner reference`() {
        assertFailsWithCode(AccountErrorCode.ACCOUNT_ACCESS_DENIED) {
            AccountAccessPolicy.checkIsOwner(accountId, listOf(owner(42L, OwnerRef.Unknown(ROMAIN_EMAIL))), ROMAIN_ID)
        }
    }
}
