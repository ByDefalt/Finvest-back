package defalt.security

import defalt.exception.AuthErrorCode
import defalt.exception.CustomException
import defalt.models.AuthenticatedUser
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

/**
 * Resolves the controller parameters annotated with [CurrentUser] to the user authenticated
 * by the [AuthenticationInterceptor].
 */
class CurrentUserArgumentResolver : HandlerMethodArgumentResolver {
    /**
     * Supports the parameters annotated with [CurrentUser] and typed [AuthenticatedUser].
     *
     * @param parameter parameter of a controller method.
     * @return true if this resolver provides the value of [parameter].
     */
    override fun supportsParameter(parameter: MethodParameter): Boolean =
        parameter.hasParameterAnnotation(CurrentUser::class.java) &&
            parameter.parameterType == AuthenticatedUser::class.java

    /**
     * Reads the user stored in the request by the [AuthenticationInterceptor].
     *
     * @param parameter parameter annotated with [CurrentUser].
     * @param mavContainer model and view of the request, not used.
     * @param webRequest current request.
     * @param binderFactory data binder factory, not used.
     * @return the [AuthenticatedUser] of the request.
     * @throws CustomException `AUTHENTICATION_REQUIRED` if no user was authenticated, i.e. the endpoint
     * is not annotated with [AuthenticationRequired].
     */
    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): Any =
        webRequest.getAttribute(
            AuthenticationInterceptor.AUTHENTICATED_USER_ATTRIBUTE,
            NativeWebRequest.SCOPE_REQUEST,
        ) as? AuthenticatedUser
            ?: throw CustomException(AuthErrorCode.AUTHENTICATION_REQUIRED, "No authenticated user for this request")
}
