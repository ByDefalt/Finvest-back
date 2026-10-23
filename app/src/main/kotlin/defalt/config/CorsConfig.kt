package defalt.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * Allows the front-end to call the API from another origin, with its credentials (refresh token cookie).
 *
 * @property allowedOrigins origin of the front-end, from the `cors.config.allowed-origins` property.
 */
@Configuration
class CorsConfig(
    @Value("\${cors.config.allowed-origins}")
    private val allowedOrigins: String,
) : WebMvcConfigurer {
    /**
     * Allows [allowedOrigins] to call every route with the usual methods, any header and the credentials.
     *
     * @param registry CORS configuration of Spring MVC.
     */
    override fun addCorsMappings(registry: CorsRegistry) {
        registry
            .addMapping("/**")
            .allowedOrigins(allowedOrigins)
            .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true)
    }
}
