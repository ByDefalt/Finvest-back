package defalt.repository

import defalt.models.ExternalUserWithPersonalInformation
import defalt.valueobject.Email
import defalt.valueobject.ExternalUserId
import defalt.valueobject.PersonalInformationId

/** Port of the persistence of the external users. */
interface ExternalUserRepository {
    /**
     * Creates an external user with the given identity.
     *
     * @param personalInformationId id of the identity of the user.
     * @return the generated id.
     */
    fun create(personalInformationId: PersonalInformationId): ExternalUserId

    /**
     * External users with the given ids, with their identity; an empty list for no id.
     *
     * @param userIds ids of the users; may be empty.
     * @return the existing external users among these ids.
     */
    fun findAllByIds(userIds: Set<ExternalUserId>): List<ExternalUserWithPersonalInformation>

    /**
     * External users with the given emails, with their identity; an empty list for no email.
     *
     * @param emails emails of the users; may be empty.
     * @return the external users using these emails.
     */
    fun findExternalUsersByEmails(emails: Set<Email>): List<ExternalUserWithPersonalInformation>
}
