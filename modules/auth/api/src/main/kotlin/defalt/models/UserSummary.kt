package defalt.models

import defalt.valueobject.Email

/**
 * Public identity of a user.
 *
 * @property firstName first name of the user.
 * @property lastName last name of the user.
 * @property email email of the user.
 */
data class UserSummary(
    val firstName: String,
    val lastName: String,
    val email: Email,
)
