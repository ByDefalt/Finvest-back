package defalt.models

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

/**
 * Identity and password of the internal user to register.
 *
 * @property email email of the user, unique.
 * @property firstName first name of the user.
 * @property lastName last name of the user.
 * @property password password of the user, stored hashed.
 */
data class RegisterUserRequest(
    @field:NotBlank
    @field:Email
    val email: String,
    @field:NotBlank
    val firstName: String,
    @field:NotBlank
    val lastName: String,
    @field:NotBlank
    val password: String,
)
