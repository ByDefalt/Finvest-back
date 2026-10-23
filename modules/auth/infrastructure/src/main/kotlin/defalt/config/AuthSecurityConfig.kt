package defalt.config

import defalt.security.BCryptPasswordHasher
import defalt.service.PasswordHasher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

/** Security services of the auth module. */
@Configuration
class AuthSecurityConfig {
    /**
     * Hashes the passwords with BCrypt.
     *
     * @return the BCrypt password hasher.
     */
    @Bean
    fun passwordHasher(): PasswordHasher = BCryptPasswordHasher(BCryptPasswordEncoder())
}
