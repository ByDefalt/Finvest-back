package defalt.persistence.repository

import defalt.models.User
import defalt.models.UserWithCredentials
import defalt.persistence.mapper.toDomain
import defalt.persistence.mapper.toDomainWithCredentials
import defalt.persistence.mapper.toUserId
import defalt.persistence.models.UserEntity
import defalt.persistence.queries.UserQueries
import defalt.repository.UserRepository
import defalt.valueobject.PersonalInformationId
import defalt.valueobject.UserId
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository

@Repository
class UserJdbcRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
) : UserRepository {
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
                ?: throw IllegalStateException("Failed to retrieve generated user ID")

        return id.toUserId()
    }

    override fun findCredentialsByEmail(email: String): UserWithCredentials? =
        jdbcTemplate
            .query(
                UserQueries.FIND_CREDENTIALS_BY_EMAIL,
                MapSqlParameterSource("email", email),
            ) { rs, _ ->
                UserEntity(
                    id = rs.getLong("id"),
                    personalInformationId = rs.getLong("personal_information_id"),
                    password = rs.getString("password"),
                ).toDomainWithCredentials()
            }.firstOrNull()

    override fun findUserByEmail(email: String): User? =
        jdbcTemplate
            .query(
                UserQueries.FIND_BY_EMAIL,
                MapSqlParameterSource("email", email),
            ) { rs, _ ->
                UserEntity(
                    id = rs.getLong("id"),
                    personalInformationId = rs.getLong("personal_information_id"),
                    password = "",
                ).toDomain()
            }.firstOrNull()

    override fun findUserById(userId: UserId): User? =
        jdbcTemplate
            .query(
                UserQueries.FIND_BY_ID,
                MapSqlParameterSource("userId", userId.value),
            ) { rs, _ ->
                UserEntity(
                    id = rs.getLong("id"),
                    personalInformationId = rs.getLong("personal_information_id"),
                    password = rs.getString("password"),
                ).toDomain()
            }.firstOrNull()
}
