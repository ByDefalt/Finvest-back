package defalt.models

import defalt.valueobject.Email
import defalt.valueobject.UserId

/**
 * User authenticated by the access token of the current request.
 *
 * @property userId id of the internal user.
 * @property email email of the user.
 */
data class AuthenticatedUser(
    val userId: UserId,
    val email: Email,
)
