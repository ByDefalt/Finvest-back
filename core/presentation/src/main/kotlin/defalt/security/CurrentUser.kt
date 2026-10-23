package defalt.security

/** Controller parameter that receives the `AuthenticatedUser` of the request, resolved from its access token. */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class CurrentUser
