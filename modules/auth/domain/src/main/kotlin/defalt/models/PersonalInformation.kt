package defalt.models

import defalt.valueobject.Email
import defalt.valueobject.PersonalInformationId

/**
 * Identity of a user, internal or external. The email is unique among all the users.
 *
 * @property id id of the personal information.
 * @property firstName first name of the user.
 * @property lastName last name of the user.
 * @property email email of the user, unique.
 */
data class PersonalInformation(
    val id: PersonalInformationId,
    val firstName: String,
    val lastName: String,
    val email: Email,
)
