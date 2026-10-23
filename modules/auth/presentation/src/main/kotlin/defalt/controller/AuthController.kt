package defalt.controller

import defalt.logger.Logger
import defalt.mapper.toLoginResponse
import defalt.models.LoginRequest
import defalt.models.LoginResponse
import defalt.models.RegisterExternalUserRequest
import defalt.models.RegisterUserRequest
import defalt.security.PublicEndpoint
import defalt.usecase.LoginUseCase
import defalt.usecase.RefreshAccessTokenUseCase
import defalt.usecase.RegisterExternalUseCase
import defalt.usecase.RegisterUseCase
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

/**
 * Public REST API of the authentication. The refresh token is kept in an HTTP-only cookie restricted
 * to `/auth/refresh`, out of reach of the JavaScript code; the access token is returned in the body.
 *
 * @param secure whether the refresh token cookie is only sent over HTTPS (`config.secure`).
 * @param registerUseCase registers the internal users.
 * @param loginUseCase logs the internal users in.
 * @param refreshAccessTokenUseCase exchanges the refresh tokens for access tokens.
 * @param registerExternalUseCase registers the external users.
 * @param logger logger of the class.
 */
@RestController
@RequestMapping("/auth")
@PublicEndpoint
class AuthController(
    @Value("\${config.secure}")
    private val secure: Boolean,
    private val registerUseCase: RegisterUseCase,
    private val loginUseCase: LoginUseCase,
    private val refreshAccessTokenUseCase: RefreshAccessTokenUseCase,
    private val registerExternalUseCase: RegisterExternalUseCase,
    private val logger: Logger,
) {
    /**
     * `POST /auth/register/external`: registers an external user. Answers `201 Created`.
     *
     * @param user identity of the external user.
     */
    @PostMapping("/register/external")
    @ResponseStatus(HttpStatus.CREATED)
    fun registerExternal(
        @Valid @RequestBody user: RegisterExternalUserRequest,
    ) {
        logger.debug("start for email=${user.email}")
        registerExternalUseCase(
            user.firstName,
            user.lastName,
            user.email,
        )
        logger.info("success email=${user.email}")
    }

    /**
     * `POST /auth/register`: registers an internal user. Answers `201 Created`.
     *
     * @param user identity and password of the internal user.
     */
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

    /**
     * `POST /auth/login`: checks the credentials and sets the refresh token cookie.
     *
     * @param loginRequest credentials of the user.
     * @param response HTTP response, where the refresh token cookie is written.
     */
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

    /**
     * `POST /auth/logout`: deletes the refresh token cookie. Answers `204 No Content`.
     *
     * @param response HTTP response, where the refresh token cookie is written.
     */
    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun logout(response: HttpServletResponse) {
        response.addHeader(
            HttpHeaders.SET_COOKIE,
            deleteRefreshCookie().toString(),
        )
    }

    /**
     * `POST /auth/refresh`: exchanges the refresh token cookie for a new access token.
     *
     * @param refreshToken refresh token, from the `refreshToken` cookie.
     * @return the new access token.
     */
    @PostMapping("/refresh")
    fun refreshAccessToken(
        @CookieValue("refreshToken") refreshToken: String,
    ): LoginResponse {
        logger.debug("start refresh token")
        val accessToken = refreshAccessTokenUseCase(refreshToken).toLoginResponse()
        logger.info("success new access token generated")
        return accessToken
    }

    /**
     * Expired refresh token cookie, which makes the browser delete it.
     *
     * @return the expired cookie.
     */
    private fun deleteRefreshCookie(): ResponseCookie =
        ResponseCookie
            .from("refreshToken", "")
            .httpOnly(true)
            .secure(secure)
            .sameSite("Strict")
            .path("/auth/refresh")
            .maxAge(Duration.ZERO)
            .build()

    /**
     * Refresh token cookie: HTTP only, same site, valid 30 days.
     *
     * @param refreshToken refresh token to store.
     * @return the cookie.
     */
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
