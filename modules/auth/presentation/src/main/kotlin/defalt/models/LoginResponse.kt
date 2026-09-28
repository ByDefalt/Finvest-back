package defalt.models

import jakarta.validation.constraints.NotNull

data class LoginResponse(
    @field:NotNull
    val token: String,
)
