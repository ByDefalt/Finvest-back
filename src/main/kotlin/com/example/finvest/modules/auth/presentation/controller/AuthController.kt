package com.example.finvest.modules.auth.presentation.controller

import com.example.finvest.logger.Logger
import com.example.finvest.modules.auth.application.usecase.LoginUseCase
import com.example.finvest.modules.auth.application.usecase.RefreshAccessTokenUseCase
import com.example.finvest.modules.auth.application.usecase.RegisterUseCase
import com.example.finvest.modules.auth.presentation.mapper.toDto
import com.example.finvest.modules.auth.presentation.mapper.toLoginResponse
import com.example.finvest.modules.auth.presentation.mapper.toRefreshToken
import com.example.finvest.modules.auth.presentation.models.LoginRequest
import com.example.finvest.modules.auth.presentation.models.LoginResponse
import com.example.finvest.modules.auth.presentation.models.RegisterUserRequest
import com.example.finvest.modules.auth.presentation.models.UserResponse
import com.example.finvest.modules.shared.presentation.security.PublicEndpoint
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseCookie
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.time.Duration

@RestController
@RequestMapping("/auth")
@PublicEndpoint
class AuthController(
    @Value("\${config.secure}")
    private val secure: Boolean,
    private val registerUseCase: RegisterUseCase,
    private val loginUseCase: LoginUseCase,
    private val refreshAccessTokenUseCase: RefreshAccessTokenUseCase,
    private val logger: Logger,
) {
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    fun register(
        @Valid @RequestBody user: RegisterUserRequest,
    ): UserResponse {
        logger.debug("start for email=${user.email}")
        val createdUser =
            registerUseCase(
                user.email,
                user.password,
            ).toDto()
        logger.info("success email=${createdUser.email}")
        return createdUser
    }

    @PostMapping("/login")
    fun login(
        @Valid @RequestBody loginRequest: LoginRequest,
        response: HttpServletResponse,
    ): LoginResponse {
        logger.debug("start login for email=${loginRequest.email}")
        val tokens =
            loginUseCase(
                loginRequest.email,
                loginRequest.password,
            )
        response.addHeader(
            HttpHeaders.SET_COOKIE,
            createRefreshCookie(tokens.toRefreshToken()).toString(),
        )
        logger.info("success")
        return tokens.toLoginResponse()
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun logout(response: HttpServletResponse) {
        response.addHeader(
            HttpHeaders.SET_COOKIE,
            deleteRefreshCookie().toString(),
        )
    }

    @PostMapping("/refresh")
    fun refreshAccessToken(
        @CookieValue("refreshToken") refreshToken: String,
    ): LoginResponse {
        logger.debug("start refresh token")
        val accessToken = refreshAccessTokenUseCase(refreshToken).toLoginResponse()
        logger.info("success new access token generated")
        return accessToken
    }

    private fun deleteRefreshCookie(): ResponseCookie =
        ResponseCookie
            .from("refreshToken", "")
            .httpOnly(true)
            .secure(secure)
            .sameSite("Strict")
            .path("/auth/refresh")
            .maxAge(Duration.ZERO)
            .build()

    private fun createRefreshCookie(refreshToken: String): ResponseCookie =
        ResponseCookie
            .from("refreshToken", refreshToken)
            .httpOnly(true)
            .secure(secure)
            .sameSite("Strict")
            .path("/auth/refresh")
            .maxAge(Duration.ofDays(30))
            .build()
}
