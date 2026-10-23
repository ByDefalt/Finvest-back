package defalt.fakes

import defalt.models.AuthenticatedUser
import defalt.models.ExternalUser
import defalt.models.ExternalUserWithPersonalInformation
import defalt.models.PersonalInformation
import defalt.models.User
import defalt.models.UserWithCredentials
import defalt.models.UserWithPersonalInformation
import defalt.repository.ExternalUserRepository
import defalt.repository.PersonalInformationRepository
import defalt.repository.UserRepository
import defalt.service.PasswordHasher
import defalt.service.TokenGenerator
import defalt.service.TokenValidator
import defalt.valueobject.Email
import defalt.valueobject.ExternalUserId
import defalt.valueobject.PersonalInformationId
import defalt.valueobject.UserId

class InMemoryPersonalInformationRepository : PersonalInformationRepository {
    private var nextId = 1L
    val personalInformations = linkedMapOf<PersonalInformationId, PersonalInformation>()

    /** Simulates a technical failure of the database. */
    var failOnCreate = false

    override fun create(
        firstName: String,
        lastName: String,
        email: String,
    ): PersonalInformationId {
        check(!failOnCreate) { "Database unavailable" }
        require(personalInformations.values.none { it.email.value == email }) { "Duplicate entry '$email'" }
        val id = PersonalInformationId(nextId++)
        personalInformations[id] = PersonalInformation(id, firstName, lastName, Email(email))
        return id
    }

    override fun findById(personalInformationId: PersonalInformationId): PersonalInformation? = personalInformations[personalInformationId]

    override fun existsByEmail(email: String): Boolean = personalInformations.values.any { it.email.value == email }
}

class InMemoryUserRepository(
    private val personalInformations: InMemoryPersonalInformationRepository,
) : UserRepository {
    private var nextId = 1L
    val users = linkedMapOf<UserId, UserWithCredentials>()

    override fun register(
        personalInformationId: PersonalInformationId,
        password: String,
    ): UserId {
        val id = UserId(nextId++)
        users[id] = UserWithCredentials(id, personalInformationId, password)
        return id
    }

    override fun findCredentialsByEmail(email: String): UserWithCredentials? =
        users.values.find { personalInformations.findById(it.personalInformationId)?.email?.value == email }

    override fun findUsersByEmails(emails: Set<Email>): List<UserWithPersonalInformation> =
        withPersonalInformation().filter { it.personalInformation.email in emails }

    override fun findUserById(userId: UserId): User? = users[userId]?.let { User(it.id, it.personalInformationId) }

    override fun findAllByIds(userIds: Set<UserId>): List<UserWithPersonalInformation> =
        withPersonalInformation().filter { it.user.id in userIds }

    private fun withPersonalInformation() =
        users.values.map {
            UserWithPersonalInformation(User(it.id, it.personalInformationId), personalInformations.findById(it.personalInformationId)!!)
        }
}

class InMemoryExternalUserRepository(
    private val personalInformations: InMemoryPersonalInformationRepository,
) : ExternalUserRepository {
    private var nextId = 1L
    val externalUsers = linkedMapOf<ExternalUserId, ExternalUser>()

    override fun create(personalInformationId: PersonalInformationId): ExternalUserId {
        val id = ExternalUserId(nextId++)
        externalUsers[id] = ExternalUser(id, personalInformationId)
        return id
    }

    override fun findAllByIds(userIds: Set<ExternalUserId>): List<ExternalUserWithPersonalInformation> =
        withPersonalInformation().filter { it.externalUser.id in userIds }

    override fun findExternalUsersByEmails(emails: Set<Email>): List<ExternalUserWithPersonalInformation> =
        withPersonalInformation().filter { it.personalInformation.email in emails }

    private fun withPersonalInformation() =
        externalUsers.values.map { ExternalUserWithPersonalInformation(it, personalInformations.findById(it.personalInformationId)!!) }
}

/** Reversible "hash" so that tests can read what was stored. */
class FakePasswordHasher : PasswordHasher {
    override fun hash(password: String): String = "hashed:$password"

    override fun matches(
        password: String,
        hash: String,
    ): Boolean = hash == hash(password)
}

/** Tokens are readable strings: "<type>:<userId>:<email>". */
class FakeTokenService :
    TokenGenerator,
    TokenValidator {
    override fun generateAccessToken(
        userId: UserId,
        email: Email,
    ): String = "access:${userId.value}:${email.value}"

    override fun generateRefreshToken(
        userId: UserId,
        email: Email,
    ): String = "refresh:${userId.value}:${email.value}"

    override fun validateAccessToken(token: String): AuthenticatedUser? =
        parse(token, "access")?.let { (id, email) -> AuthenticatedUser(UserId(id), Email(email)) }

    override fun validateRefreshToken(token: String): Long? = parse(token, "refresh")?.first

    private fun parse(
        token: String,
        type: String,
    ): Pair<Long, String>? {
        val parts = token.split(":")
        if (parts.size != 3 || parts[0] != type) return null
        return parts[1].toLong() to parts[2]
    }
}
