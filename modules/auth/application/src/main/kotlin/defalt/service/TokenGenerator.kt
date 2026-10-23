package defalt.service

import defalt.valueobject.Email
import defalt.valueobject.UserId

/** Port of the generation of the authentication tokens. */
interface TokenGenerator {
    /**
     * Short-lived token authenticating the requests of the user.
     *
     * @param userId id of the internal user.
     * @param email email of the user, written in the token.
     * @return the signed access token.
     */
    fun generateAccessToken(
        userId: UserId,
        email: Email,
    ): String

    /**
     * Long-lived token exchanged for new access tokens.
     *
     * @param userId id of the internal user.
     * @param email email of the user, written in the token.
     * @return the signed refresh token.
     */
    fun generateRefreshToken(
        userId: UserId,
        email: Email,
    ): String
}
