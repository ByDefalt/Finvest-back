package defalt.security

import org.junit.jupiter.api.Test
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class BCryptPasswordHasherTest {
    private val hasher = BCryptPasswordHasher(BCryptPasswordEncoder(4))

    @Test
    fun `hashes are salted and never contain the password`() {
        val first = hasher.hash("secret")
        val second = hasher.hash("secret")

        assertNotEquals(first, second)
        assertFalse(first.contains("secret"))
        assertTrue(first.startsWith("$2a$"))
    }

    @Test
    fun `matches only the original password`() {
        val hash = hasher.hash("secret")

        assertTrue(hasher.matches("secret", hash))
        assertFalse(hasher.matches("Secret", hash))
    }
}
