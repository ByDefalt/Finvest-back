package defalt.models

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

/**
 * Identity of the external user to register.
 *
 * @property email email of the user, unique.
 * @property firstName first name of the user.
 * @property lastName last name of the user.
 */
data class RegisterExternalUserRequest(
    @field:NotBlank
    @field:Email
    val email: String,
    @field:NotBlank
    val firstName: String,
    @field:NotBlank
    val lastName: String,
)
