package defalt.security

import defalt.service.PasswordHasher
import org.springframework.security.crypto.password.PasswordEncoder

class BCryptPasswordHasher(
    private val passwordEncoder: PasswordEncoder,
) : PasswordHasher {
    override fun hash(password: String): String = passwordEncoder.encode(password) as String

    override fun matches(
        password: String,
        hash: String,
    ): Boolean = passwordEncoder.matches(password, hash)
}
