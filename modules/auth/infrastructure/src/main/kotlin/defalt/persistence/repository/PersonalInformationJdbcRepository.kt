package defalt.persistence.repository

import defalt.logger.Logger
import defalt.models.PersonalInformation
import defalt.persistence.mapper.toDomain
import defalt.persistence.mapper.toPersonalInformationId
import defalt.persistence.models.PersonalInformationEntity
import defalt.persistence.queries.PersonalInformationQueries
import defalt.repository.PersonalInformationRepository
import defalt.valueobject.PersonalInformationId
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository

@Repository
class PersonalInformationJdbcRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val logger: Logger
) : PersonalInformationRepository {
    override fun create(
        firstName: String,
        lastName: String,
        email: String
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
                ?: throw IllegalStateException("Failed to retrieve generated user ID")

        return id.toPersonalInformationId()
    }

    override fun findById(personalInformationId: PersonalInformationId): PersonalInformation? {
        return jdbcTemplate
            .query(
                PersonalInformationQueries.GET_PERSONAL_INFO_BY_ID,
                MapSqlParameterSource("id", personalInformationId.value),
            ) { rs, _ ->
                PersonalInformationEntity(
                    id = rs.getLong("id"),
                    firstName = rs.getString("first_name"),
                    lastName = rs.getString("last_name"),
                    email = rs.getString("email"),
                ).toDomain()
            }.firstOrNull()
    }
}