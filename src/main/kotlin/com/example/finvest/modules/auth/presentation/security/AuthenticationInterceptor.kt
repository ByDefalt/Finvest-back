package com.example.finvest.modules.auth.presentation.security

import com.example.finvest.modules.auth.application.service.TokenValidator
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor

@Component
class AuthenticationInterceptor(
    private val tokenValidator: TokenValidator
) : HandlerInterceptor {

    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any
    ): Boolean {

        if (handler !is HandlerMethod) {
            return true
        }

        val method = handler.method
        val controller = handler.beanType

        val authenticationRequired =
            method.isAnnotationPresent(AuthenticationRequired::class.java) ||
                    controller.isAnnotationPresent(AuthenticationRequired::class.java)

        // Route publique
        if (!authenticationRequired) {
            return true
        }

        val authHeader = request.getHeader("Authorization")

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.sendError(
                HttpServletResponse.SC_UNAUTHORIZED,
                "Authentification requise"
            )
            return false
        }

        val token = authHeader
            .substring("Bearer ".length)
            .trim()

        try {
            val user = tokenValidator.validateAccessToken(token)

            if (user != null) {
                request.setAttribute("authenticatedUser", user)

                return true
            } else {
                response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Token invalide ou expiré"
                )
                return false
            }

        } catch (e: Exception) {
            response.sendError(
                HttpServletResponse.SC_UNAUTHORIZED,
                "Token invalide ou expiré"
            )
            return false
        }
    }
}