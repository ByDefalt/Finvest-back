package defalt.security

import defalt.logger.NullLogger
import defalt.models.AuthenticatedUser
import defalt.valueobject.Email
import defalt.valueobject.UserId
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.junit.jupiter.api.Test
import java.util.Date
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNull

class JwtTokenServiceTest {
    private val secret = "une-cle-secrete-tres-longue-d-au-moins-32-caracteres"
    private val service = JwtTokenService(NullLogger(), secret, accessExpiration = 60_000, refreshExpiration = 120_000)
    private val userId = UserId(7L)
    private val email = Email("romain@example.com")

    @Test
    fun `an access token gives back the authenticated user`() {
        val token = service.generateAccessToken(userId, email)

        assertEquals(AuthenticatedUser(userId, email), service.validateAccessToken(token))
    }

    @Test
    fun `a refresh token gives back the user id`() {
        val token = service.generateRefreshToken(userId, email)

        assertEquals(7L, service.validateRefreshToken(token))
    }

    @Test
    fun `access and refresh tokens are not interchangeable`() {
        assertNotEquals(service.generateAccessToken(userId, email), service.generateRefreshToken(userId, email))
        assertNull(service.validateAccessToken(service.generateRefreshToken(userId, email)))
        assertNull(service.validateRefreshToken(service.generateAccessToken(userId, email)))
    }

    @Test
    fun `an expired token is rejected`() {
        val expired = JwtTokenService(NullLogger(), secret, accessExpiration = -1_000, refreshExpiration = -1_000)

        assertNull(expired.validateAccessToken(expired.generateAccessToken(userId, email)))
        assertNull(expired.validateRefreshToken(expired.generateRefreshToken(userId, email)))
    }

    @Test
    fun `a token signed with another key is rejected`() {
        val other = JwtTokenService(NullLogger(), "une-autre-cle-secrete-tres-longue-de-32-caracteres", 60_000, 60_000)

        assertNull(service.validateAccessToken(other.generateAccessToken(userId, email)))
    }

    @Test
    fun `a malformed token is rejected`() {
        assertNull(service.validateAccessToken("not-a-jwt"))
        assertNull(service.validateRefreshToken(""))
    }

    @Test
    fun `an access token without a numeric subject or an email is rejected`() {
        assertNull(service.validateAccessToken(signedToken(subject = "not-a-number", email = email.value)))
        assertNull(service.validateAccessToken(signedToken(subject = "7", email = null)))
        assertNull(service.validateRefreshToken(signedToken(subject = null, email = email.value, type = "refresh")))
    }

    private fun signedToken(
        subject: String?,
        email: String?,
        type: String = "access",
    ): String {
        val builder = Jwts.builder().expiration(Date(System.currentTimeMillis() + 60_000)).claim("type", type)
        subject?.let { builder.subject(it) }
        email?.let { builder.claim("email", it) }
        return builder.signWith(Keys.hmacShaKeyFor(secret.toByteArray(Charsets.UTF_8))).compact()
    }
}
