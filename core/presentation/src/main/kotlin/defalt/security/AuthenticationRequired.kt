package defalt.security

/**
 * Endpoint (or every endpoint of a controller) that requires a valid access token in the
 * `Authorization: Bearer` header. Every endpoint must declare either this annotation or [PublicEndpoint].
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class AuthenticationRequired
