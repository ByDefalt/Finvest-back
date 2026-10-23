package defalt.resolver

import defalt.exception.AccountErrorCode
import defalt.fakes.FakeUserQueryApi
import defalt.fixtures.AccountFixtures.JOHN_EMAIL
import defalt.fixtures.AccountFixtures.JOHN_EXTERNAL_ID
import defalt.fixtures.AccountFixtures.ROMAIN_EMAIL
import defalt.fixtures.AccountFixtures.ROMAIN_ID
import defalt.fixtures.assertFailsWithCode
import defalt.valueobject.Email
import defalt.valueobject.ExternalUserId
import defalt.valueobject.OwnerRef
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UserQueryAccountOwnerResolverTest {
    private val users =
        FakeUserQueryApi()
            .withInternal(ROMAIN_ID, ROMAIN_EMAIL)
            .withExternal(JOHN_EXTERNAL_ID, JOHN_EMAIL)
    private val resolver = UserQueryAccountOwnerResolver(users)

    @Test
    fun `resolves internal and external users`() {
        assertEquals(
            mapOf(ROMAIN_EMAIL to OwnerRef.Internal(ROMAIN_ID), JOHN_EMAIL to OwnerRef.External(JOHN_EXTERNAL_ID)),
            resolver.resolve(setOf(ROMAIN_EMAIL, JOHN_EMAIL)),
        )
    }

    @Test
    fun `prefers the internal user when an email is also known as external`() {
        users.withExternal(ExternalUserId(2L), ROMAIN_EMAIL)

        assertEquals(mapOf(ROMAIN_EMAIL to OwnerRef.Internal(ROMAIN_ID)), resolver.resolve(setOf(ROMAIN_EMAIL)))
    }

    @Test
    fun `only looks up external users for emails that are not internal`() {
        resolver.resolve(setOf(ROMAIN_EMAIL, JOHN_EMAIL))

        assertEquals(listOf(setOf(JOHN_EMAIL)), users.externalIdLookups)
    }

    @Test
    fun `skips the external lookup when every email is internal`() {
        resolver.resolve(setOf(ROMAIN_EMAIL))

        assertTrue(users.externalIdLookups.isEmpty())
    }

    @Test
    fun `fails with every unknown email`() {
        val exception =
            assertFailsWithCode(AccountErrorCode.OWNER_NOT_FOUND) {
                resolver.resolve(setOf(ROMAIN_EMAIL, Email("a@example.com"), Email("b@example.com")))
            }

        assertEquals(setOf("a@example.com", "b@example.com"), (exception.params["emails"] as List<*>).toSet())
    }
}
