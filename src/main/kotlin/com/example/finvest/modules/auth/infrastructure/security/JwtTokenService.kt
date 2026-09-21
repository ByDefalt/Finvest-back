package com.example.finvest.modules.auth.infrastructure.security

import com.example.finvest.logger.Logger
import com.example.finvest.modules.auth.application.service.TokenGenerator
import com.example.finvest.modules.auth.application.service.TokenValidator
import com.example.finvest.modules.shared.application.security.AuthenticatedUser
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys.hmacShaKeyFor
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.SecretKey

@Service
class JwtTokenService(
    private val logger: Logger,

    @Value("\${jwt.secret}")
    secret: String,

    @Value("\${jwt.access-expiration}")
    private val accessExpiration: Long,

    @Value("\${jwt.refresh-expiration}")
    private val refreshExpiration: Long,
) : TokenGenerator, TokenValidator {

    private val secretKey: SecretKey = hmacShaKeyFor(secret.toByteArray(Charsets.UTF_8))

    override fun generateAccessToken(userId: Long, email: String): String =
        generateToken(
            userId = userId,
            email = email,
            type = TYPE_ACCESS,
            expiration = accessExpiration
        )

    override fun generateRefreshToken(userId: Long): String =
        generateToken(
            userId = userId,
            email = null,
            type = TYPE_REFRESH,
            expiration = refreshExpiration
        )

    override fun validateAccessToken(token: String): AuthenticatedUser? {
        val claims = parseClaims(token, TYPE_ACCESS) ?: return null
        val userId = claims.subject?.toLongOrNull() ?: return null
        val email = claims.get(CLAIM_EMAIL, String::class.java) ?: return null

        return AuthenticatedUser(userId, email)
    }

    override fun validateRefreshToken(token: String): Long? =
        parseClaims(token, TYPE_REFRESH)?.subject?.toLongOrNull()

    private fun generateToken(
        userId: Long,
        email: String?,
        type: String,
        expiration: Long
    ): String {
        val now = Date()

        val builder = Jwts.builder()
            .subject(userId.toString())
            .issuedAt(now)
            .expiration(Date(now.time + expiration))
            .claim(CLAIM_TYPE, type)

        email?.let { builder.claim(CLAIM_EMAIL, it) }

        return builder
            .signWith(secretKey)
            .compact()
    }

    private fun parseClaims(token: String, expectedType: String): Claims? =
        try {
            val claims = parseToken(token)

            if (claims.get(CLAIM_TYPE, String::class.java) == expectedType) {
                claims
            } else {
                logger.warn("JwtServiceImpl - unexpected token type, expected $expectedType")
                null
            }
        } catch (e: ExpiredJwtException) {
            // Cas normal, pas de log
            null
        } catch (e: Exception) {
            logger.warn("JwtServiceImpl - invalid $expectedType token: ${e.message}")
            null
        }

    private fun parseToken(token: String): Claims =
        Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .payload

    private companion object {
        const val CLAIM_TYPE = "type"
        const val CLAIM_EMAIL = "email"
        const val TYPE_ACCESS = "access"
        const val TYPE_REFRESH = "refresh"
    }
}