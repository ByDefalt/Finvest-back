package defalt.models

/**
 * Internal user with its identity.
 *
 * @property user the user.
 * @property personalInformation its identity.
 */
data class UserWithPersonalInformation(
    val user: User,
    val personalInformation: PersonalInformation,
)
