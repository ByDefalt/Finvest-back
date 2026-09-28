package defalt.repository

import defalt.models.User
import defalt.models.UserWithCredentials
import defalt.valueobject.PersonalInformationId
import defalt.valueobject.UserId


interface UserRepository {
    fun register(
        personalInformationId: PersonalInformationId,
        password: String,
    ): UserId

    fun findCredentialsByEmail(email: String): UserWithCredentials?

    fun findUserByEmail(email: String): User?

    fun findUserById(userId: UserId): User?
}
