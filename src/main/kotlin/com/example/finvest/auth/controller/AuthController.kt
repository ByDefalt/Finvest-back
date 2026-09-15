package com.example.finvest.auth.controller

import com.example.finvest.auth.dto.LoginRequest
import com.example.finvest.auth.dto.LoginResponse
import com.example.finvest.auth.dto.RegisterUserRequest
import com.example.finvest.auth.dto.UserResponse
import com.example.finvest.auth.mapper.toDto
import com.example.finvest.auth.service.AuthService
import com.example.finvest.common.logger.Logger
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseCookie
import org.springframework.web.bind.annotation.*
import java.time.Duration

@RestController
@RequestMapping("/auth")
class AuthController(
    @Value("\${config.secure}")
    private val secure: Boolean,
    private val authService: AuthService,
    private val logger: Logger
) {

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    fun register(
        @Valid @RequestBody user: RegisterUserRequest
    ): UserResponse {
        logger.debug("AuthController.register - start for email=${user.email}")
        val createdUser = authService.register(
            user.email,
            user.password
        )
        logger.info(
            "AuthController.register - success id=${createdUser.id} email=${createdUser.email}"
        )
        return createdUser.toDto()
    }

    @PostMapping("/login")
    fun login(
        @Valid @RequestBody loginRequest: LoginRequest,
        response: HttpServletResponse
    ): LoginResponse {
        logger.debug(
            "AuthController.login - start for email=${loginRequest.email}"
        )
        val tokens = authService.login(
            loginRequest.email,
            loginRequest.password
        )
        response.addHeader(
            HttpHeaders.SET_COOKIE,
            createRefreshCookie(tokens.refreshToken).toString()
        )
        logger.info("AuthController.login - success")
        return LoginResponse(tokens.accessToken)
    }

    @PostMapping("/refresh")
    fun refreshToken(
        @CookieValue("refreshToken") refreshToken: String
    ): LoginResponse {
        logger.debug("AuthController.refreshToken - start")
        val accessToken = authService.refreshToken(refreshToken)
        logger.info("AuthController.refreshToken - success")
        return LoginResponse(accessToken.value)
    }

    @GetMapping("/health")
    fun healthCheck(): String {
        logger.debug("AuthController.healthCheck - start")
        return "Auth service is running"
    }

    private fun createRefreshCookie(
        refreshToken: String
    ): ResponseCookie {
        return ResponseCookie.from("refreshToken", refreshToken)
            .httpOnly(true)
            .secure(secure)
            .sameSite("Strict")
            .path("/auth")
            .maxAge(Duration.ofDays(30))
            .build()
    }
}