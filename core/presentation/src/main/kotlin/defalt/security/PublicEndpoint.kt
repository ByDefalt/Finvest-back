package defalt.security

/** Endpoint (or every endpoint of a controller) open without authentication. */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class PublicEndpoint
