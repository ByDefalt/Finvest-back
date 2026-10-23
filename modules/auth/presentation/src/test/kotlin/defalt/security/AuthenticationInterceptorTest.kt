package defalt.security

import defalt.exception.AuthErrorCode
import defalt.exception.CustomException
import defalt.logger.NullLogger
import defalt.models.AuthenticatedUser
import defalt.service.TokenValidator
import defalt.valueobject.Email
import defalt.valueobject.UserId
import org.junit.jupiter.api.Test
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.web.method.HandlerMethod
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AuthenticationInterceptorTest {
    private val user = AuthenticatedUser(UserId(1L), Email("romain@example.com"))
    private val tokenValidator =
        object : TokenValidator {
            override fun validateAccessToken(token: String): AuthenticatedUser? =
                when (token) {
                    "valid" -> user
                    "crash" -> error("unexpected")
                    else -> null
                }

            override fun validateRefreshToken(token: String): Long? = null
        }
    private val interceptor = AuthenticationInterceptor(tokenValidator, NullLogger())
    private val response = MockHttpServletResponse()

    @Test
    fun `lets through handlers that are not controller methods`() {
        assertTrue(interceptor.preHandle(MockHttpServletRequest(), response, Any()))
    }

    @Test
    fun `lets through endpoints that do not require authentication`() {
        assertTrue(interceptor.preHandle(MockHttpServletRequest(), response, handler(MixedController(), "open")))
        assertEquals(200, response.status)
    }

    @Test
    fun `authenticates a valid bearer token and exposes the user to the controller`() {
        val request = request("Bearer valid")

        assertTrue(interceptor.preHandle(request, response, handler(SecuredController(), "endpoint")))
        assertEquals(user, request.getAttribute("authenticatedUser"))
    }

    @Test
    fun `requires authentication on annotated methods`() {
        assertRejected(MockHttpServletRequest(), AuthErrorCode.AUTHENTICATION_REQUIRED, handler(MixedController(), "secured"))
    }

    @Test
    fun `rejects a request without authorization header`() {
        assertRejected(MockHttpServletRequest(), AuthErrorCode.AUTHENTICATION_REQUIRED)
    }

    @Test
    fun `rejects a non bearer authorization header`() {
        assertRejected(request("Basic dXNlcjpwYXNz"), AuthErrorCode.AUTHENTICATION_REQUIRED)
    }

    @Test
    fun `rejects an invalid token`() {
        assertRejected(request("Bearer invalid"), AuthErrorCode.INVALID_ACCESS_TOKEN)
    }

    @Test
    fun `rejects the request when the token validation fails`() {
        assertRejected(request("Bearer crash"), AuthErrorCode.INVALID_ACCESS_TOKEN)
    }

    private fun assertRejected(
        request: MockHttpServletRequest,
        code: AuthErrorCode,
        handler: HandlerMethod = handler(SecuredController(), "endpoint"),
    ) {
        val exception = assertFailsWith<CustomException> { interceptor.preHandle(request, response, handler) }

        assertEquals(code, exception.code)
        assertNull(request.getAttribute("authenticatedUser"))
    }

    private fun request(authorization: String) = MockHttpServletRequest().apply { addHeader("Authorization", authorization) }

    private fun handler(
        controller: Any,
        method: String,
    ) = HandlerMethod(controller, controller.javaClass.getMethod(method))

    @AuthenticationRequired
    class SecuredController {
        fun endpoint() = Unit
    }

    class MixedController {
        @AuthenticationRequired
        fun secured() = Unit

        fun open() = Unit
    }
}
