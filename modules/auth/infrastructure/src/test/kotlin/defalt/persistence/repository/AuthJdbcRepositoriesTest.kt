package defalt.persistence.repository

import defalt.valueobject.Email
import defalt.valueobject.ExternalUserId
import defalt.valueobject.PersonalInformationId
import defalt.valueobject.UserId
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.dao.DuplicateKeyException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AuthJdbcRepositoriesTest : AuthJdbcIntegrationTest() {
    @Nested
    inner class PersonalInformations {
        @Test
        fun `creates a personal information and reads it back`() {
            val id = personalInformationRepository.create("Jane", "Smith", "jane@example.com")

            val saved = assertNotNull(personalInformationRepository.findById(id))
            assertEquals(id, saved.id)
            assertEquals("Jane", saved.firstName)
            assertEquals("Smith", saved.lastName)
            assertEquals(Email("jane@example.com"), saved.email)
        }

        @Test
        fun `returns null for an unknown id`() {
            assertNull(personalInformationRepository.findById(PersonalInformationId(-1L)))
        }

        @Test
        fun `tells whether an email is already used by an internal or external user`() {
            assertTrue(personalInformationRepository.existsByEmail(ROMAIN))
            assertTrue(personalInformationRepository.existsByEmail(JOHN))
            assertFalse(personalInformationRepository.existsByEmail("ghost@example.com"))
        }

        @Test
        fun `refuses a duplicate email`() {
            assertFailsWith<DuplicateKeyException> { personalInformationRepository.create("Other", "Romain", ROMAIN) }
        }
    }

    @Nested
    inner class Users {
        @Test
        fun `registers a user with its password and finds its credentials by email`() {
            val personalInformationId = personalInformationRepository.create("Jane", "Smith", "jane@example.com")

            val id = userRepository.register(personalInformationId, "bcrypt-hash")

            val credentials = assertNotNull(userRepository.findCredentialsByEmail("jane@example.com"))
            assertEquals(id, credentials.id)
            assertEquals(personalInformationId, credentials.personalInformationId)
            assertEquals("bcrypt-hash", credentials.password)
        }

        @Test
        fun `finds no credentials for an unknown or external email`() {
            assertNull(userRepository.findCredentialsByEmail("ghost@example.com"))
            assertNull(userRepository.findCredentialsByEmail(JOHN))
        }

        @Test
        fun `finds a user by id`() {
            assertEquals(personalInformationId(ROMAIN), userRepository.findUserById(userId(ROMAIN))?.personalInformationId)
            assertNull(userRepository.findUserById(UserId(-1L)))
        }

        @Test
        fun `finds users with their personal information by emails`() {
            val users = userRepository.findUsersByEmails(setOf(Email(ROMAIN), Email(JOHN), Email("ghost@example.com")))

            val romain = users.single()
            assertEquals(userId(ROMAIN), romain.user.id)
            assertEquals("Romain", romain.personalInformation.firstName)
            assertEquals("Rousval", romain.personalInformation.lastName)
            assertEquals(Email(ROMAIN), romain.personalInformation.email)
        }

        @Test
        fun `finds users with their personal information by ids`() {
            val users = userRepository.findAllByIds(setOf(userId(ROMAIN), userId(TEST)))

            assertEquals(setOf(Email(ROMAIN), Email(TEST)), users.map { it.personalInformation.email }.toSet())
            assertTrue(users.all { it.user.personalInformationId == it.personalInformation.id })
        }

        @Test
        fun `empty sets find no user`() {
            assertTrue(userRepository.findUsersByEmails(emptySet()).isEmpty())
            assertTrue(userRepository.findAllByIds(emptySet()).isEmpty())
        }
    }

    @Nested
    inner class ExternalUsers {
        @Test
        fun `creates an external user and finds it by id and by email`() {
            val personalInformationId = personalInformationRepository.create("Jane", "Smith", "jane@example.com")

            val id = externalUserRepository.create(personalInformationId)

            val byId = externalUserRepository.findAllByIds(setOf(id)).single()
            assertEquals(personalInformationId, byId.externalUser.personalInformationId)
            assertEquals("Jane", byId.personalInformation.firstName)
            assertEquals(Email("jane@example.com"), byId.personalInformation.email)
            assertEquals(
                listOf(id),
                externalUserRepository.findExternalUsersByEmails(setOf(Email("jane@example.com"))).map { it.externalUser.id },
            )
        }

        @Test
        fun `only finds external users`() {
            assertEquals(
                listOf(externalUserId(JOHN)),
                externalUserRepository.findExternalUsersByEmails(setOf(Email(JOHN), Email(ROMAIN))).map { it.externalUser.id },
            )
            assertTrue(externalUserRepository.findAllByIds(setOf(ExternalUserId(-1L))).isEmpty())
        }

        @Test
        fun `empty sets find no external user`() {
            assertTrue(externalUserRepository.findAllByIds(emptySet()).isEmpty())
            assertTrue(externalUserRepository.findExternalUsersByEmails(emptySet()).isEmpty())
        }
    }
}
