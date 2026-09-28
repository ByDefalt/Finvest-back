package defalt.repository

import defalt.models.PersonalInformation
import defalt.valueobject.PersonalInformationId

interface PersonalInformationRepository {
    fun create(firstName: String, lastName: String, email: String): PersonalInformationId
    fun findById(personalInformationId: PersonalInformationId): PersonalInformation?
}