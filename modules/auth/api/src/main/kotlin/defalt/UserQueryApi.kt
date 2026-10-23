package defalt

import defalt.models.UserSummary
import defalt.valueobject.Email
import defalt.valueobject.ExternalUserId
import defalt.valueobject.UserId

/**
 * Public API of the auth module: the only way for the other modules to read the users.
 * Internal users have credentials and can log in; external users do not (co-owners of accounts...).
 */
interface UserQueryApi {
    /**
     * Identity of the internal users with the given ids; unknown ids are absent from the map.
     *
     * @param userIds ids of the users; may be empty.
     * @return the identity of each existing internal user.
     */
    fun findInternalSummaries(userIds: Set<UserId>): Map<UserId, UserSummary>

    /**
     * Identity of the external users with the given ids; unknown ids are absent from the map.
     *
     * @param userIds ids of the users; may be empty.
     * @return the identity of each existing external user.
     */
    fun findExternalSummaries(userIds: Set<ExternalUserId>): Map<ExternalUserId, UserSummary>

    /**
     * Ids of the internal users with the given emails; unknown emails are absent from the map.
     *
     * @param emails emails of the users; may be empty.
     * @return the id of each email used by an internal user.
     */
    fun findInternalIds(emails: Set<Email>): Map<Email, UserId>

    /**
     * Ids of the external users with the given emails; unknown emails are absent from the map.
     *
     * @param emails emails of the users; may be empty.
     * @return the id of each email used by an external user.
     */
    fun findExternalIds(emails: Set<Email>): Map<Email, ExternalUserId>
}
