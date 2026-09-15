package com.example.finvest.auth.repository

import com.example.finvest.auth.entity.UserEntity
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository

@Repository
class AuthJdbcRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) {

    fun register(email: String, password: String): UserEntity {
        val keyHolder = GeneratedKeyHolder()

        val params = MapSqlParameterSource()
            .addValue("email", email)
            .addValue("password", password)

        jdbcTemplate.update(
            AuthQueries.REGISTER,
            params,
            keyHolder,
            arrayOf("id")
        )

        val id = keyHolder.key?.toLong()
            ?: throw IllegalStateException("Failed to retrieve generated user ID")

        return UserEntity(
            id = id,
            email = email,
            password = password
        )
    }

    fun findByEmail(email: String): UserEntity? {
        return jdbcTemplate.query(
            AuthQueries.FIND_BY_EMAIL,
            MapSqlParameterSource("email", email)
        ) { rs, _ ->
            UserEntity(
                id = rs.getLong("id"),
                email = rs.getString("email"),
                password = rs.getString("password")
            )
        }.firstOrNull()
    }

    fun findById(userId: Long): UserEntity? {
        return jdbcTemplate.query(
            AuthQueries.FIND_BY_ID,
            MapSqlParameterSource("userId", userId)
        ) { rs, _ ->
            UserEntity(
                id = rs.getLong("id"),
                email = rs.getString("email"),
                password = rs.getString("password")
            )
        }.firstOrNull()
    }
}