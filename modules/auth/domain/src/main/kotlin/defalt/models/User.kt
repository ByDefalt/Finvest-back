package defalt.models

import defalt.valueobject.PersonalInformationId
import defalt.valueobject.UserId

/**
 * Internal user: a user with credentials, who can log in.
 *
 * @property id id of the user.
 * @property personalInformationId identity of the user.
 */
data class User(
    val id: UserId,
    val personalInformationId: PersonalInformationId,
)
