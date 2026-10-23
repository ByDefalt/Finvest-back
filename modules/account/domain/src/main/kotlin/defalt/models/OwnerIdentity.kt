package defalt.models

import defalt.valueobject.Email

/**
 * Identity of an account owner, provided by the auth module.
 *
 * @property firstName first name of the owner.
 * @property lastName last name of the owner.
 * @property email email of the owner.
 */
data class OwnerIdentity(
    val firstName: String,
    val lastName: String,
    val email: Email,
)
