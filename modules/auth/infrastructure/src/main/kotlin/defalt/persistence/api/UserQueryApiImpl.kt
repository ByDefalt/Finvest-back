package defalt.persistence.api

import defalt.UserQueryApi
import defalt.models.PersonalInformation
import defalt.models.UserSummary
import defalt.repository.ExternalUserRepository
import defalt.repository.UserRepository
import defalt.valueobject.Email
import defalt.valueobject.ExternalUserId
import defalt.valueobject.UserId
import org.springframework.stereotype.Component

/**
 * Implementation of the public [UserQueryApi] of the auth module, on top of its repositories.
 *
 * @param userRepository persistence of the internal users.
 * @param externalUserRepository persistence of the external users.
 */
@Component
internal class UserQueryApiImpl(
    private val userRepository: UserRepository,
    private val externalUserRepository: ExternalUserRepository,
) : UserQueryApi {
    /**
     * Identity of the internal users with the given ids; unknown ids are absent from the map.
     *
     * @param userIds ids of the users; may be empty.
     * @return the identity of each existing internal user.
     */
    override fun findInternalSummaries(userIds: Set<UserId>): Map<UserId, UserSummary> =
        userRepository.findAllByIds(userIds).associate {
            it.user.id to it.personalInformation.toSummary()
        }

    /**
     * Identity of the external users with the given ids; unknown ids are absent from the map.
     *
     * @param userIds ids of the users; may be empty.
     * @return the identity of each existing external user.
     */
    override fun findExternalSummaries(userIds: Set<ExternalUserId>): Map<ExternalUserId, UserSummary> =
        externalUserRepository.findAllByIds(userIds).associate {
            it.externalUser.id to it.personalInformation.toSummary()
        }

    /**
     * Ids of the internal users with the given emails; unknown emails are absent from the map.
     *
     * @param emails emails of the users; may be empty.
     * @return the id of each email used by an internal user.
     */
    override fun findInternalIds(emails: Set<Email>): Map<Email, UserId> =
        userRepository.findUsersByEmails(emails).associate {
            it.personalInformation.email to it.user.id
        }

    /**
     * Ids of the external users with the given emails; unknown emails are absent from the map.
     *
     * @param emails emails of the users; may be empty.
     * @return the id of each email used by an external user.
     */
    override fun findExternalIds(emails: Set<Email>): Map<Email, ExternalUserId> =
        externalUserRepository.findExternalUsersByEmails(emails).associate {
            it.personalInformation.email to it.externalUser.id
        }
}

/**
 * Public identity of these personal information.
 *
 * @receiver identity of a user.
 * @return its public identity.
 */
private fun PersonalInformation.toSummary(): UserSummary =
    UserSummary(
        firstName = firstName,
        lastName = lastName,
        email = email,
    )
