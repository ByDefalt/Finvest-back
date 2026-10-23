package defalt.security

import defalt.service.PasswordHasher
import org.springframework.security.crypto.password.PasswordEncoder

/**
 * [PasswordHasher] based on a Spring Security encoder (BCrypt in the application).
 *
 * @param passwordEncoder Spring Security encoder doing the hashing.
 */
class BCryptPasswordHasher(
    private val passwordEncoder: PasswordEncoder,
) : PasswordHasher {
    /**
     * Salted hash of [password], to be stored instead of the password.
     *
     * @param password password in clear text.
     * @return the salted hash of the password.
     */
    override fun hash(password: String): String = passwordEncoder.encode(password) as String

    /**
     * Whether [password] is the password hashed in [hash].
     *
     * @param password password in clear text.
     * @param hash hash stored for the user.
     * @return true if the password matches the hash.
     */
    override fun matches(
        password: String,
        hash: String,
    ): Boolean = passwordEncoder.matches(password, hash)
}
