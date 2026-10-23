package defalt.models

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

/**
 * Credentials of an internal user.
 *
 * @property email email of the user.
 * @property password password of the user.
 */
data class LoginRequest(
    @field:NotBlank
    @field:Email
    val email: String,
    @field:NotBlank
    val password: String,
)
