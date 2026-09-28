package defalt.controller

import defalt.exception.CommonErrorCode
import defalt.exception.CustomException
import defalt.exception.ErrorCode
import defalt.logger.Logger
import defalt.mapper.toLoginResponse
import defalt.models.LoginRequest
import defalt.models.LoginResponse
import defalt.models.RegisterUserRequest
import defalt.security.PublicEndpoint
import defalt.usecase.LoginUseCase
import defalt.usecase.RefreshAccessTokenUseCase
import defalt.usecase.RegisterUseCase
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.Duration
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseCookie
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

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
    ) {
        logger.debug("start for email=${user.email}")
        registerUseCase(
            user.email,
            user.firstName,
            user.lastName,
            user.password,
            )
        logger.info("success email=${user.email}")
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    fun login(
        @Valid @RequestBody loginRequest: LoginRequest,
        response: HttpServletResponse,
    ) {
        logger.debug("start login for email=${loginRequest.email}")
        val token =
            loginUseCase(
                loginRequest.email,
                loginRequest.password,
            )
        response.addHeader(
            HttpHeaders.SET_COOKIE,
            createRefreshCookie(token.value).toString(),
        )
        logger.info("success")
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
