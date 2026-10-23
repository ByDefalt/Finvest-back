package defalt.models

/**
 * External user with its identity.
 *
 * @property externalUser the external user.
 * @property personalInformation its identity.
 */
data class ExternalUserWithPersonalInformation(
    val externalUser: ExternalUser,
    val personalInformation: PersonalInformation,
)
