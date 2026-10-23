package defalt.persistence.repository

import defalt.exception.CommonErrorCode
import defalt.exception.CustomException
import defalt.models.PersonalInformation
import defalt.models.User
import defalt.models.UserWithCredentials
import defalt.models.UserWithPersonalInformation
import defalt.persistence.queries.UserQueries
import defalt.repository.UserRepository
import defalt.valueobject.Email
import defalt.valueobject.PersonalInformationId
import defalt.valueobject.UserId
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository

/**
 * JDBC adapter of the [UserRepository] port, on the `users` table.
 *
 * @param jdbcTemplate runs the SQL queries.
 */
@Repository
class UserJdbcRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
) : UserRepository {
    /**
     * Creates an internal user with the given identity.
     *
     * @param password hash of the password, never the password itself.
     * @param personalInformationId id of the identity of the user.
     * @return the generated id.
     */
    override fun register(
        personalInformationId: PersonalInformationId,
        password: String,
    ): UserId {
        val keyHolder = GeneratedKeyHolder()

        jdbcTemplate.update(
            UserQueries.REGISTER,
            MapSqlParameterSource()
                .addValue("personalInformationId", personalInformationId.value)
                .addValue("password", password),
            keyHolder,
            arrayOf("id"),
        )

        val id =
            keyHolder.key?.toLong()
                ?: throw CustomException(CommonErrorCode.INTERNAL_ERROR, "Failed to retrieve the generated id")

        return UserId(id)
    }

    /**
     * Credentials of the internal user with [email], or null if there is none.
     *
     * @param email email of the user.
     * @return the credentials, or null if no internal user uses [email].
     */
    override fun findCredentialsByEmail(email: String): UserWithCredentials? =
        jdbcTemplate
            .query(
                UserQueries.FIND_CREDENTIALS_BY_EMAIL,
                MapSqlParameterSource("email", email),
            ) { rs, _ ->
                UserWithCredentials(
                    id = UserId(rs.getLong("id")),
                    personalInformationId = PersonalInformationId(rs.getLong("personal_information_id")),
                    password = rs.getString("password"),
                )
            }.firstOrNull()

    /**
     * Internal users with the given emails, with their identity; an empty list for no email.
     *
     * @param emails emails of the users; may be empty.
     * @return the internal users using these emails.
     */
    override fun findUsersByEmails(emails: Set<Email>): List<UserWithPersonalInformation> =
        if (emails.isEmpty()) {
            emptyList()
        } else {
            jdbcTemplate.query(
                UserQueries.FIND_ALL_BY_EMAILS,
                MapSqlParameterSource("emails", emails.map(Email::value)),
            ) { rs, _ ->
                UserWithPersonalInformation(
                    user =
                        User(
                            id = UserId(rs.getLong("id")),
                            personalInformationId = PersonalInformationId(rs.getLong("personal_information_id")),
                        ),
                    personalInformation =
                        PersonalInformation(
                            id = PersonalInformationId(rs.getLong("personal_information_id")),
                            firstName = rs.getString("first_name"),
                            lastName = rs.getString("last_name"),
                            email = Email(rs.getString("email")),
                        ),
                )
            }
        }

    /**
     * The internal user, or null if it does not exist.
     *
     * @param userId id of the internal user.
     * @return the internal user, or null if it does not exist.
     */
    override fun findUserById(userId: UserId): User? =
        jdbcTemplate
            .query(
                UserQueries.FIND_BY_ID,
                MapSqlParameterSource("userId", userId.value),
            ) { rs, _ ->
                User(
                    id = UserId(rs.getLong("id")),
                    personalInformationId = PersonalInformationId(rs.getLong("personal_information_id")),
                )
            }.firstOrNull()

    /**
     * Internal users with the given ids, with their identity; an empty list for no id.
     *
     * @param userIds ids of the users; may be empty.
     * @return the existing internal users among these ids.
     */
    override fun findAllByIds(userIds: Set<UserId>): List<UserWithPersonalInformation> =
        if (userIds.isEmpty()) {
            emptyList()
        } else {
            jdbcTemplate.query(
                UserQueries.FIND_ALL_BY_IDS,
                MapSqlParameterSource("userIds", userIds.map(UserId::value)),
            ) { rs, _ ->
                UserWithPersonalInformation(
                    user =
                        User(
                            id = UserId(rs.getLong("id")),
                            personalInformationId = PersonalInformationId(rs.getLong("personal_information_id")),
                        ),
                    personalInformation =
                        PersonalInformation(
                            id = PersonalInformationId(rs.getLong("personal_information_id")),
                            firstName = rs.getString("first_name"),
                            lastName = rs.getString("last_name"),
                            email = Email(rs.getString("email")),
                        ),
                )
            }
        }
}
