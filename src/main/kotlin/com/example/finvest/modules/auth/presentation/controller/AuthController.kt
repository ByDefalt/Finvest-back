package com.example.finvest.modules.auth.presentation.controller


import com.example.finvest.logger.Logger
import com.example.finvest.modules.auth.application.usecase.LoginUseCase
import com.example.finvest.modules.auth.application.usecase.RefreshAccessTokenUseCase
import com.example.finvest.modules.auth.application.usecase.RegisterUseCase
import com.example.finvest.modules.auth.presentation.dto.LoginRequest
import com.example.finvest.modules.auth.presentation.dto.LoginResponse
import com.example.finvest.modules.auth.presentation.dto.RegisterUserRequest
import com.example.finvest.modules.auth.presentation.dto.UserResponse
import com.example.finvest.modules.auth.presentation.mapper.toDto
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
    private val registerUseCase: RegisterUseCase,
    private val loginUseCase: LoginUseCase,
    private val refreshAccessTokenUseCase: RefreshAccessTokenUseCase,
    private val logger: Logger
) {

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    fun register(
        @Valid @RequestBody user: RegisterUserRequest
    ): UserResponse {
        logger.debug("start for email=${user.email}")
        val createdUser = registerUseCase(
            user.email,
            user.password
        )
        logger.info("success id=${createdUser.id} email=${createdUser.email}")
        return createdUser.toDto()
    }

    @PostMapping("/login")
    fun login(
        @Valid @RequestBody loginRequest: LoginRequest,
        response: HttpServletResponse
    ): LoginResponse {
        logger.debug("start for email=${loginRequest.email}")
        val tokens = loginUseCase(
            loginRequest.email,
            loginRequest.password
        )
        response.addHeader(
            HttpHeaders.SET_COOKIE,
            createRefreshCookie(tokens.refreshToken).toString()
        )
        logger.info("success")
        return LoginResponse(tokens.accessToken)
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun logout(response: HttpServletResponse) {
        response.addHeader(
            HttpHeaders.SET_COOKIE,
            deleteRefreshCookie().toString()
        )
    }

    private fun deleteRefreshCookie(): ResponseCookie {
        return ResponseCookie.from("refreshToken", "")
            .httpOnly(true)
            .secure(secure)
            .sameSite("Strict")
            .path("/auth")
            .maxAge(Duration.ZERO)
            .build()
    }

    @PostMapping("/refresh")
    fun refreshToken(
        @CookieValue("refreshToken") refreshToken: String
    ): LoginResponse {
        logger.debug("start")
        val accessToken = refreshAccessTokenUseCase(refreshToken)
        logger.info("success")
        return LoginResponse(accessToken.value)
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