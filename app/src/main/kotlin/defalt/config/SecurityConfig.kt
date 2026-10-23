package defalt.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

/**
 * Spring Security configuration. Authentication is not handled by Spring Security but by the
 * `AuthenticationInterceptor` of the auth module, so every request is permitted here.
 */
@Configuration
@EnableWebSecurity
class SecurityConfig {
    /**
     * Disables CSRF (stateless JWT API) and allows the H2 console to be displayed in a frame of the same origin.
     *
     * @param http Spring Security configuration builder of the HTTP requests.
     * @return the security filter chain of the application.
     */
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .headers {
                it.frameOptions { frame ->
                    frame.sameOrigin()
                }
            }.authorizeHttpRequests {
                it.anyRequest().permitAll()
            }

        return http.build()
    }
}
