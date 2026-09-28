package com.example.finvest.modules.auth.infrastructure.config

import com.example.finvest.modules.auth.application.service.PasswordHasher
import com.example.finvest.modules.auth.infrastructure.security.BCryptPasswordHasher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
class AuthSecurityConfig {
    @Bean
    fun passwordHasher(): PasswordHasher = BCryptPasswordHasher(BCryptPasswordEncoder())
}
