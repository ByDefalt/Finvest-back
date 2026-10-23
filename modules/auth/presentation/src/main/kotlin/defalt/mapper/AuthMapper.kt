package defalt.mapper

import defalt.models.LoginResponse
import defalt.valueobject.AccessToken

/**
 * Response body carrying the access token.
 *
 * @receiver the new access token.
 * @return the response body.
 */
fun AccessToken.toLoginResponse(): LoginResponse =
    LoginResponse(
        token = this.value,
    )
