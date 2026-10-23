package defalt.security

import defalt.logger.Logger
import defalt.models.AuthenticatedUser
import defalt.service.TokenGenerator
import defalt.service.TokenValidator
import defalt.valueobject.Email
import defalt.valueobject.UserId
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys.hmacShaKeyFor
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.Date
import javax.crypto.SecretKey

/**
 * Generates and validates the JWTs of the application, signed with HMAC-SHA. The subject of a token is
 * the user id; its claims give the user email and the token type (`access` or `refresh`), so that a
 * refresh token is never accepted as an access token and conversely.
 *
 * @param secret signing key (`jwt.secret`), at least 32 characters.
 * @param accessExpiration lifetime of the access tokens in milliseconds (`jwt.access-expiration`).
 * @param refreshExpiration lifetime of the refresh tokens in milliseconds (`jwt.refresh-expiration`).
 * @param logger logger of the class.
 */
@Service
class JwtTokenService(
    private val logger: Logger,
    @Value("\${jwt.secret}")
    secret: String,
    @Value("\${jwt.access-expiration}")
    private val accessExpiration: Long,
    @Value("\${jwt.refresh-expiration}")
    private val refreshExpiration: Long,
) : TokenGenerator,
    TokenValidator {
    private val secretKey: SecretKey = hmacShaKeyFor(secret.toByteArray(Charsets.UTF_8))

    /**
     * Short-lived token authenticating the requests of the user.
     *
     * @param userId id of the internal user.
     * @param email email of the user, written in the token.
     * @return the signed access token.
     */
    override fun generateAccessToken(
        userId: UserId,
        email: Email,
    ): String =
        generateToken(
            userId = userId.value,
            email = email.value,
            type = TYPE_ACCESS,
            expiration = accessExpiration,
        )

    /**
     * Long-lived token exchanged for new access tokens.
     *
     * @param userId id of the internal user.
     * @param email email of the user, written in the token.
     * @return the signed refresh token.
     */
    override fun generateRefreshToken(
        userId: UserId,
        email: Email,
    ): String =
        generateToken(
            userId = userId.value,
            email = email.value,
            type = TYPE_REFRESH,
            expiration = refreshExpiration,
        )

    /**
     * User authenticated by [token], or null if it is not a valid and unexpired access token.
     *
     * @param token access token sent by the client.
     * @return the authenticated user, or null if the token is not valid.
     */
    override fun validateAccessToken(token: String): AuthenticatedUser? {
        val claims = parseClaims(token, TYPE_ACCESS) ?: return null
        val userId = claims.subject?.toLongOrNull() ?: return null
        val email = claims.get(CLAIM_EMAIL, String::class.java) ?: return null

        return AuthenticatedUser(UserId(userId), Email(email))
    }

    /**
     * Id of the user of [token], or null if it is not a valid and unexpired refresh token.
     *
     * @param token refresh token sent by the client.
     * @return the id of the user, or null if the token is not valid.
     */
    override fun validateRefreshToken(token: String): Long? = parseClaims(token, TYPE_REFRESH)?.subject?.toLongOrNull()

    /**
     * Signed token of [type] for the user, valid for [expiration] milliseconds.
     *
     * @param userId id of the user, the subject of the token.
     * @param email email of the user.
     * @param type type of the token, `access` or `refresh`.
     * @param expiration lifetime of the token, in milliseconds.
     * @return the signed token.
     */
    private fun generateToken(
        userId: Long,
        email: String,
        type: String,
        expiration: Long,
    ): String {
        val now = Date()

        val builder =
            Jwts
                .builder()
                .subject(userId.toString())
                .issuedAt(now)
                .expiration(Date(now.time + expiration))
                .claim(CLAIM_TYPE, type)
                .claim(CLAIM_EMAIL, email)

        return builder
            .signWith(secretKey)
            .compact()
    }

    /**
     * Claims of [token] if it is valid, unexpired and of [expectedType], null otherwise.
     * Expired tokens are a normal case and are not logged.
     *
     * @param token token sent by the client.
     * @param expectedType type the token must have.
     * @return the claims of the token, or null if it is not valid.
     */
    private fun parseClaims(
        token: String,
        expectedType: String,
    ): Claims? =
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

    /**
     * Claims of [token], after checking its signature and expiration.
     *
     * @param token token sent by the client.
     * @return the claims of the token.
     * @throws io.jsonwebtoken.JwtException if the token is malformed, expired or wrongly signed.
     */
    private fun parseToken(token: String): Claims =
        Jwts
            .parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .payload

    private companion object {
        /** Claim holding the token type. */
        const val CLAIM_TYPE = "type"

        /** Claim holding the user email. */
        const val CLAIM_EMAIL = "email"

        /** Type of the access tokens. */
        const val TYPE_ACCESS = "access"

        /** Type of the refresh tokens. */
        const val TYPE_REFRESH = "refresh"
    }
}
