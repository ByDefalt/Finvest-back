package defalt.models

import jakarta.validation.constraints.NotNull

/**
 * New access token.
 *
 * @property token access token, to send in the `Authorization: Bearer` header.
 */
data class LoginResponse(
    @field:NotNull
    val token: String,
)
