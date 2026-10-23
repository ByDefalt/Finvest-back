package defalt.persistence.repository

import defalt.exception.CommonErrorCode
import defalt.exception.CustomException
import defalt.logger.Logger
import defalt.models.PersonalInformation
import defalt.persistence.queries.PersonalInformationQueries
import defalt.repository.PersonalInformationRepository
import defalt.valueobject.Email
import defalt.valueobject.PersonalInformationId
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository

/**
 * JDBC adapter of the [PersonalInformationRepository] port, on the `personal_informations` table.
 *
 * @param jdbcTemplate runs the SQL queries.
 * @param logger logger of the class.
 */
@Repository
class PersonalInformationJdbcRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val logger: Logger,
) : PersonalInformationRepository {
    /**
     * Creates the identity of a user.
     *
     * @param firstName first name of the user.
     * @param lastName last name of the user.
     * @param email email of the user, unique.
     * @return the generated id.
     */
    override fun create(
        firstName: String,
        lastName: String,
        email: String,
    ): PersonalInformationId {
        val keyHolder = GeneratedKeyHolder()

        jdbcTemplate.update(
            PersonalInformationQueries.CREATE_PERSONAL_INFORMATION,
            MapSqlParameterSource()
                .addValue("firstName", firstName)
                .addValue("lastName", lastName)
                .addValue("email", email),
            keyHolder,
            arrayOf("id"),
        )

        val id =
            keyHolder.key?.toLong()
                ?: throw CustomException(CommonErrorCode.INTERNAL_ERROR, "Failed to retrieve the generated id")

        return PersonalInformationId(id)
    }

    /**
     * The identity, or null if it does not exist.
     *
     * @param personalInformationId id of the identity of the user.
     * @return the identity, or null if it does not exist.
     */
    override fun findById(personalInformationId: PersonalInformationId): PersonalInformation? =
        jdbcTemplate
            .query(
                PersonalInformationQueries.GET_PERSONAL_INFO_BY_ID,
                MapSqlParameterSource("id", personalInformationId.value),
            ) { rs, _ ->
                PersonalInformation(
                    id = PersonalInformationId(rs.getLong("id")),
                    firstName = rs.getString("first_name"),
                    lastName = rs.getString("last_name"),
                    email = Email(rs.getString("email")),
                )
            }.firstOrNull()

    /**
     * Whether an internal or external user already uses [email].
     *
     * @param email email of the user.
     * @return true if the email is already used.
     */
    override fun existsByEmail(email: String): Boolean =
        jdbcTemplate.queryForObject(
            PersonalInformationQueries.EXISTS_BY_EMAIL,
            MapSqlParameterSource("email", email),
            Int::class.java,
        )!! > 0
}
