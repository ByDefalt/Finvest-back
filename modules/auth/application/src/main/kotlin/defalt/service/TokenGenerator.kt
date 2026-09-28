package defalt.service

import defalt.valueobject.Email
import defalt.valueobject.UserId

interface TokenGenerator {
    fun generateAccessToken(
        userId: UserId,
        email: Email,
    ): String

    fun generateRefreshToken(userId: UserId, email: Email): String
}
