package defalt.config

import defalt.security.AuthenticationInterceptor
import defalt.security.CurrentUserArgumentResolver
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * Registers the authentication of the auth module in Spring MVC: the interceptor that validates the
 * access tokens and the resolver of the `@CurrentUser` parameters.
 *
 * @param authenticationInterceptor validates the access tokens of the protected endpoints.
 */
@Configuration
class WebConfig(
    private val authenticationInterceptor: AuthenticationInterceptor,
) : WebMvcConfigurer {
    /**
     * Registers the interceptor validating the access tokens on every route.
     *
     * @param registry interceptors of Spring MVC.
     */
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(authenticationInterceptor)
    }

    /**
     * Registers the resolver of the `@CurrentUser` parameters.
     *
     * @param resolvers argument resolvers of Spring MVC.
     */
    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(CurrentUserArgumentResolver())
    }
}
