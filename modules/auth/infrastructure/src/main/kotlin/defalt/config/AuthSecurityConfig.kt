package defalt.config


import defalt.security.BCryptPasswordHasher
import defalt.service.PasswordHasher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
class AuthSecurityConfig {
    @Bean
    fun passwordHasher(): PasswordHasher = BCryptPasswordHasher(BCryptPasswordEncoder())
}
