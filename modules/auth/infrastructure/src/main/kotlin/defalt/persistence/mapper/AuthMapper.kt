package defalt.persistence.mapper


import defalt.models.PersonalInformation
import defalt.models.User
import defalt.models.UserWithCredentials
import defalt.persistence.models.PersonalInformationEntity
import defalt.persistence.models.UserEntity
import defalt.valueobject.Email
import defalt.valueobject.PersonalInformationId
import defalt.valueobject.UserId

fun UserEntity.toDomain(): User =
    User(
        id = UserId(id),
        personalInformationId = PersonalInformationId(personalInformationId),
    )

fun UserEntity.toDomainWithCredentials(): UserWithCredentials =
    UserWithCredentials(
        id = UserId(this.id),
        personalInformationId = PersonalInformationId(this.personalInformationId),
        password = this.password,
    )

fun PersonalInformationEntity.toDomain(): PersonalInformation =
    PersonalInformation(
        id = PersonalInformationId(this.id),
        firstName = this.firstName,
        lastName = this.lastName,
        email = Email(this.email),
    )

fun Long.toUserId(): UserId = UserId(this)
fun Long.toPersonalInformationId(): PersonalInformationId = PersonalInformationId(this)
