package defalt.persistence.api

import defalt.UserQueryApi
import defalt.models.UserSummary
import defalt.persistence.repository.AuthJdbcIntegrationTest
import defalt.valueobject.Email
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** Public contract of the auth module, used by the account module to resolve owners. */
class UserQueryApiImplTest : AuthJdbcIntegrationTest() {
    @Autowired
    private lateinit var userQueryApi: UserQueryApi

    @Test
    fun `finds the ids of internal users by email`() {
        assertEquals(
            mapOf(Email(ROMAIN) to userId(ROMAIN)),
            userQueryApi.findInternalIds(setOf(Email(ROMAIN), Email(JOHN), Email("ghost@example.com"))),
        )
    }

    @Test
    fun `finds the ids of external users by email`() {
        assertEquals(
            mapOf(Email(JOHN) to externalUserId(JOHN)),
            userQueryApi.findExternalIds(setOf(Email(ROMAIN), Email(JOHN))),
        )
    }

    @Test
    fun `finds the summaries of internal users by id`() {
        assertEquals(
            mapOf(
                userId(ROMAIN) to UserSummary("Romain", "Rousval", Email(ROMAIN)),
                userId(TEST) to UserSummary("Test", "User", Email(TEST)),
            ),
            userQueryApi.findInternalSummaries(setOf(userId(ROMAIN), userId(TEST))),
        )
    }

    @Test
    fun `finds the summaries of external users by id`() {
        assertEquals(
            mapOf(externalUserId(JOHN) to UserSummary("John", "Doe", Email(JOHN))),
            userQueryApi.findExternalSummaries(setOf(externalUserId(JOHN))),
        )
    }

    @Test
    fun `empty sets give empty maps`() {
        assertTrue(userQueryApi.findInternalIds(emptySet()).isEmpty())
        assertTrue(userQueryApi.findExternalIds(emptySet()).isEmpty())
        assertTrue(userQueryApi.findInternalSummaries(emptySet()).isEmpty())
        assertTrue(userQueryApi.findExternalSummaries(emptySet()).isEmpty())
    }
}
