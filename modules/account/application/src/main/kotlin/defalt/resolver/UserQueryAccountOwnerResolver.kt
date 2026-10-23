package defalt.resolver

import defalt.UserQueryApi
import defalt.exception.AccountErrorCode
import defalt.exception.CustomException
import defalt.valueobject.Email
import defalt.valueobject.OwnerRef

/**
 * [AccountOwnerResolver] based on the public API of the auth module.
 * The internal users win over the external users with the same email, and the external users are only
 * looked up for the emails that are not internal.
 *
 * @param userQueryApi public API of the auth module, to read the users.
 */
class UserQueryAccountOwnerResolver(
    private val userQueryApi: UserQueryApi,
) : AccountOwnerResolver {
    /**
     * Resolves each email to an internal user or, failing that, to an external user.
     *
     * @param emails emails of the owners.
     * @return the owner of every email.
     * @throws defalt.exception.CustomException `OWNER_NOT_FOUND` if an email matches no user, with the unknown `emails`.
     */
    override fun resolve(emails: Set<Email>): Map<Email, OwnerRef> {
        val internal = userQueryApi.findInternalIds(emails).mapValues { OwnerRef.Internal(it.value) }
        val remaining = emails - internal.keys
        val external =
            if (remaining.isEmpty()) {
                emptyMap()
            } else {
                userQueryApi.findExternalIds(remaining).mapValues { OwnerRef.External(it.value) }
            }

        val unknown = remaining - external.keys
        if (unknown.isNotEmpty()) {
            throw CustomException(
                code = AccountErrorCode.OWNER_NOT_FOUND,
                message = "Owners not found in internal or external users",
            ).setProperty("emails", unknown.map(Email::value)).lock()
        }

        return internal + external
    }
}
