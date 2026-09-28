package defalt.models

import defalt.valueobject.Email
import defalt.valueobject.PersonalInformationId

data class PersonalInformation(
    val id: PersonalInformationId,
    val firstName: String,
    val lastName: String,
    val email: Email,
)
