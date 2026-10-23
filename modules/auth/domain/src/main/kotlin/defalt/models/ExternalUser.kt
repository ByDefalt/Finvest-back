package defalt.models

import defalt.valueobject.ExternalUserId
import defalt.valueobject.PersonalInformationId

/**
 * User without credentials, who cannot log in but can co-own accounts.
 *
 * @property id id of the external user.
 * @property personalInformationId identity of the user.
 */
data class ExternalUser(
    val id: ExternalUserId,
    val personalInformationId: PersonalInformationId,
)
