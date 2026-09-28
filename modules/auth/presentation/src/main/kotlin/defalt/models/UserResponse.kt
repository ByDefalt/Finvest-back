package defalt.models

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull

data class UserResponse(
    @field:NotNull
    val firstName: String,
    @field:NotNull
    val lastName: String,
    @field:NotNull
    @field:Email
    val email: String,
)
