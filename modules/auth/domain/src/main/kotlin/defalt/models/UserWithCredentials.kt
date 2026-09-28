package defalt.models

import defalt.valueobject.PersonalInformationId
import defalt.valueobject.UserId

data class UserWithCredentials(
    val id: UserId,
    val personalInformationId: PersonalInformationId,
    val password: String,
)
