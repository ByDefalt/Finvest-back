package defalt.persistence.repository

import defalt.exception.CommonErrorCode
import defalt.exception.CustomException
import defalt.logger.Logger
import defalt.models.ExternalUser
import defalt.models.ExternalUserWithPersonalInformation
import defalt.models.PersonalInformation
import defalt.persistence.queries.ExternalUserQueries
import defalt.repository.ExternalUserRepository
import defalt.valueobject.Email
import defalt.valueobject.ExternalUserId
import defalt.valueobject.PersonalInformationId
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository

/**
 * JDBC adapter of the [ExternalUserRepository] port, on the `users_external` table.
 *
 * @param jdbcTemplate runs the SQL queries.
 * @param logger logger of the class.
 */
@Repository
class ExternalUserJdbcRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val logger: Logger,
) : ExternalUserRepository {
    /**
     * Creates an external user with the given identity.
     *
     * @param personalInformationId id of the identity of the user.
     * @return the generated id.
     */
    override fun create(personalInformationId: PersonalInformationId): ExternalUserId {
        val keyHolder = GeneratedKeyHolder()

        jdbcTemplate.update(
            ExternalUserQueries.CREATE_EXTERNAL_USER,
            MapSqlParameterSource()
                .addValue("personalInformationId", personalInformationId.value),
            keyHolder,
            arrayOf("id"),
        )

        val id =
            keyHolder.key?.toLong()
                ?: throw CustomException(CommonErrorCode.INTERNAL_ERROR, "Failed to retrieve the generated id")

        return ExternalUserId(id)
    }

    /**
     * External users with the given ids, with their identity; an empty list for no id.
     *
     * @param userIds ids of the users; may be empty.
     * @return the existing external users among these ids.
     */
    override fun findAllByIds(userIds: Set<ExternalUserId>): List<ExternalUserWithPersonalInformation> =
        if (userIds.isEmpty()) {
            emptyList()
        } else {
            jdbcTemplate.query(
                ExternalUserQueries.FIND_ALL_BY_IDS,
                MapSqlParameterSource("userIds", userIds.map(ExternalUserId::value)),
            ) { rs, _ ->
                ExternalUserWithPersonalInformation(
                    externalUser =
                        ExternalUser(
                            id = ExternalUserId(rs.getLong("id")),
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
     * External users with the given emails, with their identity; an empty list for no email.
     *
     * @param emails emails of the users; may be empty.
     * @return the external users using these emails.
     */
    override fun findExternalUsersByEmails(emails: Set<Email>): List<ExternalUserWithPersonalInformation> =
        if (emails.isEmpty()) {
            emptyList()
        } else {
            jdbcTemplate.query(
                ExternalUserQueries.FIND_ALL_BY_EMAILS,
                MapSqlParameterSource("emails", emails.map(Email::value)),
            ) { rs, _ ->
                ExternalUserWithPersonalInformation(
                    externalUser =
                        ExternalUser(
                            id = ExternalUserId(rs.getLong("id")),
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
