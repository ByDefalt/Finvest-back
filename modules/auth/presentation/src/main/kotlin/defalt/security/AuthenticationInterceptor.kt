package defalt.security

import defalt.exception.AuthErrorCode
import defalt.exception.CustomException
import defalt.logger.Logger
import defalt.service.TokenValidator
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor

/**
 * Authenticates the requests of the endpoints annotated with [AuthenticationRequired].
 * Failures are thrown as [CustomException]s, rendered with their code by the GlobalExceptionHandler.
 *
 * @param tokenValidator validates the authentication tokens.
 * @param logger logger of the class.
 */
@Component
class AuthenticationInterceptor(
    private val tokenValidator: TokenValidator,
    private val logger: Logger,
) : HandlerInterceptor {
    /**
     * Lets the public endpoints through. For the endpoints annotated with [AuthenticationRequired]
     * (on the method or on the controller), validates the `Authorization: Bearer` access token and stores
     * the authenticated user in the request attribute [AUTHENTICATED_USER_ATTRIBUTE].
     *
     * @param request current request.
     * @param response current response, not used.
     * @param handler handler of the request: only the controller methods are checked.
     * @return true, the request always proceeds when it is not rejected.
     * @throws CustomException `AUTHENTICATION_REQUIRED` if the bearer token is missing,
     * `INVALID_ACCESS_TOKEN` if it is invalid or expired.
     */
    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
    ): Boolean {
        if (handler !is HandlerMethod) {
            return true
        }

        val authenticationRequired =
            handler.method.isAnnotationPresent(AuthenticationRequired::class.java) ||
                handler.beanType.isAnnotationPresent(AuthenticationRequired::class.java)

        // Route publique
        if (!authenticationRequired) {
            return true
        }

        val authHeader = request.getHeader("Authorization")
        if (authHeader == null || !authHeader.startsWith(BEARER)) {
            logger.info("Token missing in request")
            throw CustomException(AuthErrorCode.AUTHENTICATION_REQUIRED, "Authentication required")
        }

        val token = authHeader.substring(BEARER.length).trim()
        val user =
            runCatching { tokenValidator.validateAccessToken(token) }.getOrNull()
                ?: run {
                    logger.warn("Token invalide ou expiré")
                    throw CustomException(AuthErrorCode.INVALID_ACCESS_TOKEN, "Invalid or expired access token")
                }

        request.setAttribute(AUTHENTICATED_USER_ATTRIBUTE, user)
        logger.info("Utilisateur authentifié id=${user.userId} email=${user.email}")
        return true
    }

    /** Constants shared with [CurrentUserArgumentResolver]. */
    companion object {
        /** Request attribute holding the authenticated user, read by [CurrentUserArgumentResolver]. */
        const val AUTHENTICATED_USER_ATTRIBUTE = "authenticatedUser"
        private const val BEARER = "Bearer "
    }
}
