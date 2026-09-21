package com.example.finvest.modules.auth.presentation.security

import com.example.finvest.modules.shared.application.security.AuthenticatedUser
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

class CurrentUserArgumentResolver : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(CurrentUser::class.java) &&
                parameter.parameterType == AuthenticatedUser::class.java
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any {
        return webRequest.getAttribute(
            "authenticatedUser",
            NativeWebRequest.SCOPE_REQUEST
        ) as? AuthenticatedUser
            ?: throw IllegalStateException("Authenticated user not found")
    }
}