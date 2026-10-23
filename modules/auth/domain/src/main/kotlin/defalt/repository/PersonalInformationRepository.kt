package defalt.repository

import defalt.models.PersonalInformation
import defalt.valueobject.PersonalInformationId

/** Port of the persistence of the identities of the users. */
interface PersonalInformationRepository {
    /**
     * Creates the identity of a user.
     *
     * @param firstName first name of the user.
     * @param lastName last name of the user.
     * @param email email of the user, unique.
     * @return the generated id.
     */
    fun create(
        firstName: String,
        lastName: String,
        email: String,
    ): PersonalInformationId

    /**
     * The identity, or null if it does not exist.
     *
     * @param personalInformationId id of the identity of the user.
     * @return the identity, or null if it does not exist.
     */
    fun findById(personalInformationId: PersonalInformationId): PersonalInformation?

    /**
     * Whether an internal or external user already uses [email].
     *
     * @param email email of the user.
     * @return true if the email is already used.
     */
    fun existsByEmail(email: String): Boolean
}
