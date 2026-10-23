package defalt.repository

import defalt.models.User
import defalt.models.UserWithCredentials
import defalt.models.UserWithPersonalInformation
import defalt.valueobject.Email
import defalt.valueobject.PersonalInformationId
import defalt.valueobject.UserId

/** Port of the persistence of the internal users. */
interface UserRepository {
    /**
     * Creates an internal user with the given identity.
     *
     * @param password hash of the password, never the password itself.
     * @param personalInformationId id of the identity of the user.
     * @return the generated id.
     */
    fun register(
        personalInformationId: PersonalInformationId,
        password: String,
    ): UserId

    /**
     * Credentials of the internal user with [email], or null if there is none.
     *
     * @param email email of the user.
     * @return the credentials, or null if no internal user uses [email].
     */
    fun findCredentialsByEmail(email: String): UserWithCredentials?

    /**
     * Internal users with the given emails, with their identity; an empty list for no email.
     *
     * @param emails emails of the users; may be empty.
     * @return the internal users using these emails.
     */
    fun findUsersByEmails(emails: Set<Email>): List<UserWithPersonalInformation>

    /**
     * The internal user, or null if it does not exist.
     *
     * @param userId id of the internal user.
     * @return the internal user, or null if it does not exist.
     */
    fun findUserById(userId: UserId): User?

    /**
     * Internal users with the given ids, with their identity; an empty list for no id.
     *
     * @param userIds ids of the users; may be empty.
     * @return the existing internal users among these ids.
     */
    fun findAllByIds(userIds: Set<UserId>): List<UserWithPersonalInformation>
}
