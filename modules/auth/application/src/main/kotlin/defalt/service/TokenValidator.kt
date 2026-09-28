package defalt.service

import defalt.models.AuthenticatedUser


interface TokenValidator {
    fun validateAccessToken(token: String): AuthenticatedUser?

    fun validateRefreshToken(token: String): Long?
}
