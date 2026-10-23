package defalt.usecase

import defalt.exception.AuthErrorCode
import defalt.exception.CommonErrorCode
import defalt.exception.CustomException
import defalt.exception.ErrorCode
import defalt.fakes.FakePasswordHasher
import defalt.fakes.FakeTokenService
import defalt.fakes.InMemoryExternalUserRepository
import defalt.fakes.InMemoryPersonalInformationRepository
import defalt.fakes.InMemoryUserRepository
import defalt.logger.NullLogger
import defalt.manager.ImmediateTransactionManager
import defalt.valueobject.Email
import defalt.valueobject.PersonalInformationId
import defalt.valueobject.UserId
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertTrue

private fun assertFailsWithCode(
    code: ErrorCode,
    block: () -> Unit,
): CustomException {
    val exception = assertFailsWith<CustomException> { block() }
    assertEquals(code, exception.code)
    return exception
}

class AuthUseCasesTest {
    private val personalInformations = InMemoryPersonalInformationRepository()
    private val users = InMemoryUserRepository(personalInformations)
    private val externalUsers = InMemoryExternalUserRepository(personalInformations)
    private val passwordHasher = FakePasswordHasher()
    private val tokens = FakeTokenService()
    private val transactions = ImmediateTransactionManager()
    private val logger = NullLogger()

    private val register = RegisterUseCase(users, personalInformations, transactions, passwordHasher, logger)
    private val registerExternal = RegisterExternalUseCase(externalUsers, personalInformations, transactions, logger)
    private val login = LoginUseCase(users, passwordHasher, tokens, logger)
    private val refreshAccessToken = RefreshAccessTokenUseCase(tokens, tokens, users, personalInformations, logger)

    private fun registerRomain() = register("romain@example.com", "Romain", "Rousval", "secret")

    @Nested
    inner class Register {
        @Test
        fun `stores the personal information and the hashed password`() {
            registerRomain()

            val personalInformation = personalInformations.personalInformations.values.single()
            assertEquals("Romain", personalInformation.firstName)
            assertEquals("Rousval", personalInformation.lastName)
            assertEquals(Email("romain@example.com"), personalInformation.email)
            val user = users.users.values.single()
            assertEquals(personalInformation.id, user.personalInformationId)
            assertEquals("hashed:secret", user.password)
            assertEquals(1, transactions.executions)
        }

        @Test
        fun `rejects an email that is already used`() {
            registerRomain()

            assertFailsWithCode(AuthErrorCode.EMAIL_ALREADY_USED) { registerRomain() }
            assertEquals(1, users.users.size)
        }

        @Test
        fun `rejects the email of an external user`() {
            registerExternal("Romain", "Rousval", "romain@example.com")

            assertFailsWithCode(AuthErrorCode.EMAIL_ALREADY_USED) { registerRomain() }
            assertTrue(users.users.isEmpty())
        }

        @Test
        fun `reports a technical failure as a creation failure`() {
            personalInformations.failOnCreate = true

            val exception = assertFailsWithCode(CommonErrorCode.CREATION_FAILED) { registerRomain() }

            assertIs<IllegalStateException>(exception.cause)
            assertTrue(users.users.isEmpty())
        }
    }

    @Nested
    inner class RegisterExternal {
        @Test
        fun `stores the personal information of the external user`() {
            registerExternal("John", "Doe", "john.doe@example.com")

            val externalUser = externalUsers.externalUsers.values.single()
            assertEquals(Email("john.doe@example.com"), personalInformations.findById(externalUser.personalInformationId)?.email)
            assertTrue(users.users.isEmpty())
        }

        @Test
        fun `rejects an email that is already used`() {
            registerExternal("John", "Doe", "john.doe@example.com")

            assertFailsWithCode(AuthErrorCode.EMAIL_ALREADY_USED) { registerExternal("John", "Doe", "john.doe@example.com") }
            assertEquals(1, externalUsers.externalUsers.size)
        }

        @Test
        fun `reports a technical failure as a creation failure`() {
            personalInformations.failOnCreate = true

            assertFailsWithCode(CommonErrorCode.CREATION_FAILED) { registerExternal("John", "Doe", "john.doe@example.com") }
            assertTrue(externalUsers.externalUsers.isEmpty())
        }
    }

    @Nested
    inner class Login {
        @Test
        fun `returns a refresh token for valid credentials`() {
            registerRomain()

            val token = login("romain@example.com", "secret")

            assertEquals("refresh:1:romain@example.com", token.value)
        }

        @Test
        fun `rejects an unknown email`() {
            val exception = assertFailsWith<CustomException> { login("ghost@example.com", "secret") }

            assertEquals(AuthErrorCode.USER_NOT_FOUND, exception.code)
        }

        @Test
        fun `rejects a wrong password`() {
            registerRomain()

            val exception = assertFailsWith<CustomException> { login("romain@example.com", "wrong") }

            assertEquals(AuthErrorCode.INVALID_PASSWORD, exception.code)
        }
    }

    @Nested
    inner class RefreshAccessToken {
        @Test
        fun `returns an access token for the user of the refresh token`() {
            registerRomain()

            val accessToken = refreshAccessToken("refresh:1:romain@example.com")

            assertEquals("access:1:romain@example.com", accessToken.value)
        }

        @Test
        fun `rejects an invalid refresh token`() {
            assertFailsWithCode(AuthErrorCode.INVALID_REFRESH_TOKEN) { refreshAccessToken("access:1:romain@example.com") }
        }

        @Test
        fun `rejects a refresh token of a user that no longer exists`() {
            val exception = assertFailsWith<CustomException> { refreshAccessToken("refresh:99:ghost@example.com") }

            assertEquals(AuthErrorCode.USER_NOT_FOUND, exception.code)
        }

        @Test
        fun `fails when the personal information of the user is missing`() {
            users.register(PersonalInformationId(42L), "hashed:secret")

            assertFailsWithCode(AuthErrorCode.PERSONAL_INFORMATION_NOT_FOUND) { refreshAccessToken("refresh:1:romain@example.com") }
            assertEquals(UserId(1L), users.users.keys.single())
        }
    }
}
