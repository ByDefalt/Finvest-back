package defalt.service

import defalt.models.AuthenticatedUser

/** Port of the validation of the authentication tokens. */
interface TokenValidator {
    /**
     * User authenticated by [token], or null if it is not a valid and unexpired access token.
     *
     * @param token access token sent by the client.
     * @return the authenticated user, or null if the token is not valid.
     */
    fun validateAccessToken(token: String): AuthenticatedUser?

    /**
     * Id of the user of [token], or null if it is not a valid and unexpired refresh token.
     *
     * @param token refresh token sent by the client.
     * @return the id of the user, or null if the token is not valid.
     */
    fun validateRefreshToken(token: String): Long?
}
