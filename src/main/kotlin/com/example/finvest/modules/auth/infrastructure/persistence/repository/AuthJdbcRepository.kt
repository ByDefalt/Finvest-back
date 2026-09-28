package com.example.finvest.modules.auth.infrastructure.persistence.repository

import com.example.finvest.modules.auth.domain.models.User
import com.example.finvest.modules.auth.domain.models.UserCredentials
import com.example.finvest.modules.auth.domain.repository.AuthRepository
import com.example.finvest.modules.auth.infrastructure.persistence.mapper.toDomain
import com.example.finvest.modules.auth.infrastructure.persistence.mapper.toDomainCredentials
import com.example.finvest.modules.auth.infrastructure.persistence.models.UserEntity
import com.example.finvest.modules.auth.infrastructure.persistence.queries.AuthQueries
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository

@Repository
class AuthJdbcRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
) : AuthRepository {
    override fun register(
        email: String,
        password: String,
    ): User {
        val keyHolder = GeneratedKeyHolder()

        val params =
            MapSqlParameterSource()
                .addValue("email", email)
                .addValue("password", password)

        jdbcTemplate.update(
            AuthQueries.REGISTER,
            params,
            keyHolder,
            arrayOf("id"),
        )

        val id =
            keyHolder.key?.toLong()
                ?: throw IllegalStateException("Failed to retrieve generated user ID")

        return UserEntity(
            id = id,
            email = email,
            password = password,
        ).toDomain()
    }

    override fun findCredentialsByEmail(email: String): UserCredentials? =
        jdbcTemplate
            .query(
                AuthQueries.FIND_CREDENTIALS_BY_EMAIL,
                MapSqlParameterSource("email", email),
            ) { rs, _ ->
                UserEntity(
                    id = rs.getLong("id"),
                    email = rs.getString("email"),
                    password = rs.getString("password"),
                ).toDomainCredentials()
            }.firstOrNull()

    override fun findUserByEmail(email: String): User? =
        jdbcTemplate
            .query(
                AuthQueries.FIND_BY_EMAIL,
                MapSqlParameterSource("email", email),
            ) { rs, _ ->
                UserEntity(
                    id = rs.getLong("id"),
                    email = rs.getString("email"),
                    password = rs.getString("password"),
                ).toDomain()
            }.firstOrNull()

    override fun findUserById(userId: Long): User? =
        jdbcTemplate
            .query(
                AuthQueries.FIND_BY_ID,
                MapSqlParameterSource("userId", userId),
            ) { rs, _ ->
                UserEntity(
                    id = rs.getLong("id"),
                    email = rs.getString("email"),
                    password = rs.getString("password"),
                ).toDomain()
            }.firstOrNull()
}
