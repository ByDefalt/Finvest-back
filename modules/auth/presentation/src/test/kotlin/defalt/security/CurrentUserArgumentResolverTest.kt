package defalt.security

import defalt.exception.AuthErrorCode
import defalt.exception.CustomException
import defalt.models.AuthenticatedUser
import defalt.valueobject.Email
import defalt.valueobject.UserId
import org.junit.jupiter.api.Test
import org.springframework.core.MethodParameter
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.web.context.request.ServletWebRequest
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CurrentUserArgumentResolverTest {
    private val resolver = CurrentUserArgumentResolver()
    private val method = Endpoints::class.java.methods.single { it.name == "handle" }
    private val currentUser = MethodParameter(method, 0)
    private val notAnnotated = MethodParameter(method, 1)
    private val wrongType = MethodParameter(method, 2)

    @Test
    fun `supports only parameters annotated with CurrentUser of type AuthenticatedUser`() {
        assertTrue(resolver.supportsParameter(currentUser))
        assertFalse(resolver.supportsParameter(notAnnotated))
        assertFalse(resolver.supportsParameter(wrongType))
    }

    @Test
    fun `resolves the user authenticated by the interceptor`() {
        val user = AuthenticatedUser(UserId(1L), Email("romain@example.com"))
        val request = MockHttpServletRequest().apply { setAttribute("authenticatedUser", user) }

        assertEquals(user, resolver.resolveArgument(currentUser, null, ServletWebRequest(request), null))
    }

    @Test
    fun `fails when no user was authenticated`() {
        val exception =
            assertFailsWith<CustomException> {
                resolver.resolveArgument(currentUser, null, ServletWebRequest(MockHttpServletRequest()), null)
            }

        assertEquals(AuthErrorCode.AUTHENTICATION_REQUIRED, exception.code)
    }

    @Suppress("unused")
    class Endpoints {
        fun handle(
            @CurrentUser user: AuthenticatedUser,
            plain: AuthenticatedUser,
            @CurrentUser name: String,
        ) = Unit
    }
}
