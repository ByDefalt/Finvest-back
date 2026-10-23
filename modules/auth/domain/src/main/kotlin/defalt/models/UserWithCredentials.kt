package defalt.models

import defalt.valueobject.PersonalInformationId
import defalt.valueobject.UserId

/**
 * Internal user with its credentials, used to check a login.
 *
 * @property id id of the user.
 * @property personalInformationId identity of the user.
 * @property password hash of the password, never the password itself.
 */
data class UserWithCredentials(
    val id: UserId,
    val personalInformationId: PersonalInformationId,
    val password: String,
)
