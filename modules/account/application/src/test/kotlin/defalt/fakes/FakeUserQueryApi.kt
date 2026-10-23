package defalt.fakes

import defalt.UserQueryApi
import defalt.models.UserSummary
import defalt.valueobject.Email
import defalt.valueobject.ExternalUserId
import defalt.valueobject.UserId

class FakeUserQueryApi : UserQueryApi {
    private val internalUsers = mutableMapOf<UserId, UserSummary>()
    private val externalUsers = mutableMapOf<ExternalUserId, UserSummary>()
    val externalIdLookups = mutableListOf<Set<Email>>()

    fun withInternal(
        id: UserId,
        email: Email,
        firstName: String = "First",
        lastName: String = "Last",
    ) = apply {
        internalUsers[id] = UserSummary(firstName, lastName, email)
    }

    fun withExternal(
        id: ExternalUserId,
        email: Email,
        firstName: String = "First",
        lastName: String = "Last",
    ) = apply {
        externalUsers[id] = UserSummary(firstName, lastName, email)
    }

    override fun findInternalSummaries(userIds: Set<UserId>): Map<UserId, UserSummary> = internalUsers.filterKeys { it in userIds }

    override fun findExternalSummaries(userIds: Set<ExternalUserId>): Map<ExternalUserId, UserSummary> =
        externalUsers.filterKeys { it in userIds }

    override fun findInternalIds(emails: Set<Email>): Map<Email, UserId> =
        internalUsers.filterValues { it.email in emails }.entries.associate { (id, summary) -> summary.email to id }

    override fun findExternalIds(emails: Set<Email>): Map<Email, ExternalUserId> {
        externalIdLookups += emails
        return externalUsers.filterValues { it.email in emails }.entries.associate { (id, summary) -> summary.email to id }
    }
}
