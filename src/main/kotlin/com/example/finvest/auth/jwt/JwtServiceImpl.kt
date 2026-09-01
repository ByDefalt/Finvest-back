package com.example.finvest.auth.jwt

import com.example.finvest.common.dto.AuthenticatedUser
import com.example.finvest.common.logger.Logger
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Service
import java.util.Date
import javax.crypto.SecretKey


@Service
class JwtServiceImpl(
    val logger: Logger
) : JwtService {
    private val secretKey: SecretKey? = Jwts.SIG.HS256.key().build()

    override fun generate(userId: Long, email: String): String {
        return Jwts.builder()
            .subject(userId.toString())
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + 60 * 60 * 1000))
            .claims()
            .add("id", userId.toString())
            .add("email", email)
            .and()
            .signWith(secretKey)
            .compact()
    }

    override fun validate(token: String): AuthenticatedUser? {
        try {
            val claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
            val authenticatedUser = AuthenticatedUser(
                claims.get("id", String::class.java).toLong(),
                claims.get("email", String::class.java)
            )
            return authenticatedUser
        } catch (e: Exception) {
            logger.warn("JwtServiceImpl.validate - invalid token: ${e.message}")
            return null
        }
    }
}