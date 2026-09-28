package defalt.models

import defalt.valueobject.PersonalInformationId
import defalt.valueobject.UserId

data class User(
    val id: UserId,
    val personalInformationId: PersonalInformationId,
)
