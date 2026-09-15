package com.example.finvest.auth.jwt

import com.example.finvest.common.dto.AuthenticatedUser
import com.example.finvest.common.logger.Logger
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys.hmacShaKeyFor
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.SecretKey

@Service
class JwtServiceImpl(
    val logger: Logger,

    @Value("\${jwt.secret}")
    private val secret: String,

    @Value("\${jwt.access-expiration}")
    private val accessExpiration: Long,

    @Value("\${jwt.refresh-expiration}")
    private val refreshExpiration: Long,
) : JwtService {

    private val secretKey: SecretKey by lazy {
        hmacShaKeyFor(
            secret.toByteArray()
        )
    }

    override fun generateAccessToken(
        userId: Long,
        email: String
    ): String {
        return generateToken(
            userId = userId,
            email = email,
            type = "access",
            expiration = accessExpiration
        )
    }

    override fun generateRefreshToken(
        userId: Long
    ): String {
        return generateToken(
            userId = userId,
            email = null,
            type = "refresh",
            expiration = refreshExpiration
        )
    }

    private fun generateToken(
        userId: Long,
        email: String?,
        type: String,
        expiration: Long
    ): String {

        val builder = Jwts.builder()
            .subject(userId.toString())
            .issuedAt(Date())
            .expiration(
                Date(System.currentTimeMillis() + expiration)
            )
            .claim("type", type)

        if (email != null) {
            builder.claim("email", email)
        }

        return builder
            .signWith(secretKey)
            .compact()
    }

    override fun validateAccessToken(
        token: String
    ): AuthenticatedUser? {

        return try {
            val claims = parseToken(token)

            if (claims.get("type", String::class.java) != "access") {
                return null
            }

            AuthenticatedUser(
                claims.subject.toLong(),
                claims.get("email", String::class.java)
            )

        } catch (e: Exception) {
            logger.warn(
                "JwtServiceImpl.validateAccessToken - invalid token: ${e.message}"
            )
            null
        }
    }

    override fun validateRefreshToken(
        token: String
    ): Long? {

        return try {
            val claims = parseToken(token)

            if (claims.get("type", String::class.java) != "refresh") {
                return null
            }

            claims.subject.toLong()

        } catch (e: Exception) {
            logger.warn(
                "JwtServiceImpl.validateRefreshToken - invalid token: ${e.message}"
            )
            null
        }
    }

    private fun parseToken(token: String) =
        Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .payload
}