package defalt.exception

import defalt.logger.Logger
import jakarta.validation.ConstraintViolation
import org.springframework.http.ProblemDetail
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.FieldError
import org.springframework.web.HttpMediaTypeNotAcceptableException
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingPathVariableException
import org.springframework.web.bind.MissingRequestCookieException
import org.springframework.web.bind.MissingRequestHeaderException
import org.springframework.web.bind.MissingRequestValueException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.ServletRequestBindingException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.servlet.NoHandlerFoundException
import org.springframework.web.servlet.resource.NoResourceFoundException

/**
 * Turns every error into a `ProblemDetail` response whose `code` property is a translatable [ErrorCode]:
 * [CustomException]s, Spring MVC errors (unknown route, missing parameter...), bean validation failures
 * and unexpected errors.
 *
 * @param logger logger of the errors.
 */
@RestControllerAdvice
class GlobalExceptionHandler(
    val logger: Logger,
) {
    /**
     * Unexpected error (bug): logged with its stack trace, its message is never sent to the client.
     * Spring wraps the Errors thrown by a controller (TODO() -> NotImplementedError) in a ServletException.
     *
     * @param exception exception to turn into an error response.
     * @return a `NOT_IMPLEMENTED` response for a TODO(), an `INTERNAL_ERROR` response otherwise.
     */
    @ExceptionHandler(Exception::class)
    fun handleUnexpectedException(exception: Exception): ProblemDetail {
        if (generateSequence<Throwable>(exception) { it.cause }.any { it is NotImplementedError }) {
            logger.error("Not implemented: ${exception.message}")
            return problem(CommonErrorCode.NOT_IMPLEMENTED)
        }
        logger.error("Unexpected error: ${exception.message}", exception)
        return problem(CommonErrorCode.INTERNAL_ERROR)
    }

    /**
     * Answers with the status, the code and the params of the exception.
     * Server errors (5xx) are logged with their stack trace, client errors as warnings.
     *
     * @param exception exception to turn into an error response.
     * @return an error response with the code and the params of the exception.
     */
    @ExceptionHandler(CustomException::class)
    fun handleCustomException(exception: CustomException): ProblemDetail {
        if (exception.code.http
                .toHttpStatus()
                .is5xxServerError
        ) {
            logger.error("${exception.code.name}: ${exception.message}", exception)
        } else {
            logger.warn("${exception.code.name}: ${exception.message}")
        }
        return problem(exception.code).apply { exception.params.forEach { setProperty(it.key, it.value) } }
    }

    /**
     * Body that cannot be read (malformed JSON, unknown account type, missing field...): `INVALID_FORMAT`.
     *
     * @param exception exception to turn into an error response.
     * @return an `INVALID_FORMAT` error response.
     */
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleNotReadable(exception: HttpMessageNotReadableException): ProblemDetail {
        logger.warn("Unreadable request body: ${exception.mostSpecificCause.message}")
        return problem(CommonErrorCode.INVALID_FORMAT)
    }

    // ------------------------------------------------------------------
    // Spring MVC errors: unknown route, wrong method, missing or malformed request values
    // ------------------------------------------------------------------

    /**
     * Unknown route: `NOT_FOUND`.
     *
     * @param exception exception to turn into an error response.
     * @return a `NOT_FOUND` error response.
     */
    @ExceptionHandler(NoResourceFoundException::class)
    fun handleNoResourceFound(exception: NoResourceFoundException): ProblemDetail {
        logger.warn("No resource found: ${exception.httpMethod} /${exception.resourcePath}")
        return problem(CommonErrorCode.NOT_FOUND)
    }

    /**
     * Unknown route: `NOT_FOUND`.
     *
     * @param exception exception to turn into an error response.
     * @return a `NOT_FOUND` error response.
     */
    @ExceptionHandler(NoHandlerFoundException::class)
    fun handleNoHandlerFound(exception: NoHandlerFoundException): ProblemDetail {
        logger.warn("No handler found: ${exception.httpMethod} ${exception.requestURL}")
        return problem(CommonErrorCode.NOT_FOUND)
    }

    /**
     * HTTP method not supported by the route: `METHOD_NOT_ALLOWED`.
     *
     * @param exception exception to turn into an error response.
     * @return a `METHOD_NOT_ALLOWED` error response.
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleMethodNotSupported(exception: HttpRequestMethodNotSupportedException): ProblemDetail {
        logger.warn("Method not supported: ${exception.method}")
        return problem(CommonErrorCode.METHOD_NOT_ALLOWED)
    }

    /**
     * Body format not supported: `UNSUPPORTED_MEDIA_TYPE`.
     *
     * @param exception exception to turn into an error response.
     * @return an `UNSUPPORTED_MEDIA_TYPE` error response.
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException::class)
    fun handleMediaTypeNotSupported(exception: HttpMediaTypeNotSupportedException): ProblemDetail {
        logger.warn("Media type not supported: ${exception.contentType}")
        return problem(CommonErrorCode.UNSUPPORTED_MEDIA_TYPE)
    }

    /**
     * Response format not supported: `NOT_ACCEPTABLE`.
     *
     * @param exception exception to turn into an error response.
     * @return a `NOT_ACCEPTABLE` error response.
     */
    @ExceptionHandler(HttpMediaTypeNotAcceptableException::class)
    fun handleMediaTypeNotAcceptable(exception: HttpMediaTypeNotAcceptableException): ProblemDetail {
        logger.warn("Media type not acceptable: ${exception.message}")
        return problem(CommonErrorCode.NOT_ACCEPTABLE)
    }

    /**
     * Missing cookie, header, parameter or path variable: `REQUIRED`, with its name in `field`.
     *
     * @param exception exception to turn into an error response.
     * @return a `REQUIRED` error response, with the name of the missing value in `field`.
     */
    @ExceptionHandler(MissingRequestValueException::class)
    fun handleMissingRequestValue(exception: MissingRequestValueException): ProblemDetail {
        val name =
            when (exception) {
                is MissingRequestCookieException -> exception.cookieName
                is MissingRequestHeaderException -> exception.headerName
                is MissingServletRequestParameterException -> exception.parameterName
                is MissingPathVariableException -> exception.variableName
                else -> null
            }
        logger.warn("Missing request value: $name")
        return problem(CommonErrorCode.REQUIRED).apply { name?.let { setProperty("field", it) } }
    }

    /**
     * Request that does not match the conditions of the route: `INVALID`.
     *
     * @param exception exception to turn into an error response.
     * @return an `INVALID` error response.
     */
    @ExceptionHandler(ServletRequestBindingException::class)
    fun handleRequestBinding(exception: ServletRequestBindingException): ProblemDetail {
        logger.warn("Invalid request: ${exception.message}")
        return problem(CommonErrorCode.INVALID)
    }

    /**
     * Request value of the wrong type (`/accounts/abc`): `INVALID_FORMAT`, with its name in `field`.
     *
     * @param exception exception to turn into an error response.
     * @return an `INVALID_FORMAT` error response, with the name of the value in `field`.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleTypeMismatch(exception: MethodArgumentTypeMismatchException): ProblemDetail {
        logger.warn("Invalid value for ${exception.name}: ${exception.value}")
        return problem(CommonErrorCode.INVALID_FORMAT).apply { setProperty("field", exception.name) }
    }

    /**
     * Error response with the status and the `code` of [code].
     *
     * @param code code of the error, which gives the HTTP status.
     * @return error response with the translatable `code` of the error.
     */
    private fun problem(code: ErrorCode): ProblemDetail =
        ProblemDetail.forStatus(code.http.toHttpStatus()).apply { setProperty("code", code.name) }

    /**
     * Error on one field of a request body.
     *
     * @property field path of the field (`details.iban`, `owners[0].email`...).
     * @property code translatable code of the error.
     * @property params values of the constraint (`minLength`, `max`...).
     */
    data class FieldViolation(
        val field: String,
        val code: ErrorCode,
        val params: Map<String, Any> = emptyMap(),
    )

    /**
     * Request body breaking its constraints: `VALIDATION_FAILED`, with one entry per field in `errors`.
     *
     * @param e exception listing the fields that break their constraints.
     * @return a `VALIDATION_FAILED` error response, with one entry per field in `errors`.
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleBeanValidation(e: MethodArgumentNotValidException): ProblemDetail {
        val violations = e.bindingResult.fieldErrors.map { it.toViolation() }

        logger.warn("Validation failed: ${violations.joinToString { "${it.field}:${it.code.name}" }}")

        return problem(CommonErrorCode.VALIDATION_FAILED).apply {
            setProperty("errors", violations.map { it.toResponse() })
        }
    }

    /**
     * JSON representation of the violation in the `errors` property.
     *
     * @receiver violation to send to the client.
     * @return the `field`, `code` and `params` of the violation.
     */
    private fun FieldViolation.toResponse(): Map<String, Any> =
        buildMap {
            put("field", field)
            put("code", code.name)
            if (params.isNotEmpty()) put("params", params)
        }

    /**
     * Translates a bean validation error into a [FieldViolation] with a translatable code and the values of
     * the constraint. Errors that do not come from a bean validation constraint are `INVALID`.
     *
     * @receiver validation error of a field.
     * @return the violation with its translatable code and the values of its constraint.
     */
    private fun FieldError.toViolation(): FieldViolation {
        val attrs =
            runCatching {
                unwrap(ConstraintViolation::class.java).constraintDescriptor.attributes
            }.getOrNull() ?: return FieldViolation(field, CommonErrorCode.INVALID)

        return when (code) {
            "NotBlank", "NotNull", "NotEmpty" -> FieldViolation(field, CommonErrorCode.REQUIRED)
            "Email", "Pattern" -> FieldViolation(field, CommonErrorCode.INVALID_FORMAT)
            "Size" -> {
                val min = attrs["min"] as Int
                val len =
                    when (val value = rejectedValue) {
                        is CharSequence -> value.length
                        is Collection<*> -> value.size
                        is Map<*, *> -> value.size
                        is Array<*> -> value.size
                        else -> 0
                    }
                if (len < min) {
                    FieldViolation(field, CommonErrorCode.TOO_SHORT, mapOf("minLength" to min))
                } else {
                    FieldViolation(field, CommonErrorCode.TOO_LONG, mapOf("maxLength" to attrs["max"]!!))
                }
            }

            "Min" -> FieldViolation(field, CommonErrorCode.TOO_SMALL, mapOf("min" to attrs["value"]!!))
            "Max" -> FieldViolation(field, CommonErrorCode.TOO_LARGE, mapOf("max" to attrs["value"]!!))
            "Positive", "PositiveOrZero" -> FieldViolation(field, CommonErrorCode.TOO_SMALL, mapOf("min" to 0))
            "PastOrPresent" -> FieldViolation(field, CommonErrorCode.INVALID)
            else -> {
                logger.warn("Unmapped constraint '$code' on field '$field'")
                FieldViolation(field, CommonErrorCode.INVALID)
            }
        }
    }
}
