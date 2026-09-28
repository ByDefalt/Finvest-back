package defalt.exception

import defalt.logger.Logger
import jakarta.validation.ConstraintViolation
import jdk.jshell.spi.ExecutionControl.NotImplementedException
import org.springframework.http.ProblemDetail
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler(
    val logger: Logger,
) {
    @ExceptionHandler(Exception::class)
    fun handleUserAlreadyExists(exception: Exception): ProblemDetail {
        logger.error(exception.localizedMessage)
        val pd = ProblemDetail.forStatus(
            CommonErrorCode.INTERNAL_ERROR.http.toHttpStatus(),
        )
        pd.setProperty("code", CommonErrorCode.INTERNAL_ERROR.name)
        return pd
    }

    @ExceptionHandler(NotImplementedException::class)
    fun handleNotImplementedException(exception: NotImplementedException): ProblemDetail {
        logger.error(exception.localizedMessage)
        val pd = ProblemDetail.forStatus(
            CommonErrorCode.NOT_IMPLEMENTED.http.toHttpStatus(),
        )
        pd.setProperty("code", CommonErrorCode.NOT_IMPLEMENTED.name)
        return pd
    }

    @ExceptionHandler(CustomException::class)
    fun handleCustomException(exception: CustomException): ProblemDetail {
        logger.error(exception.localizedMessage)
        val pd = ProblemDetail.forStatus(
            exception.code.http.toHttpStatus()
        )
        pd.setProperty("code", exception.code.name)
        exception.params.forEach { pd.setProperty(it.key, it.value) }
        return pd
    }

    data class FieldViolation(
        val field: String,
        val code: ErrorCode,
        val params: Map<String, Any> = emptyMap(),
    )

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleBeanValidation(e: MethodArgumentNotValidException): ProblemDetail {
        val violations = e.bindingResult.fieldErrors.map { it.toViolation() }

        logger.warn("Validation failed: ${violations.joinToString { "${it.field}:${it.code.name}" }}")


        return ProblemDetail.forStatus(
            CommonErrorCode.VALIDATION_FAILED.http.toHttpStatus()
        ).apply {
            setProperty("code", CommonErrorCode.VALIDATION_FAILED.name)
            setProperty("errors", violations.map { it.toResponse() })
        }
    }


    private fun FieldViolation.toResponse(): Map<String, Any> = buildMap {
        put("field", field)
        put("code", code.name)
        if (params.isNotEmpty()) put("params", params)
    }

    private fun FieldError.toViolation(): FieldViolation {
        val attrs = runCatching {
            unwrap(ConstraintViolation::class.java).constraintDescriptor.attributes
        }.getOrNull() ?: return FieldViolation(field, CommonErrorCode.INVALID)

        return when (code) {
            "NotBlank", "NotNull", "NotEmpty" -> FieldViolation(field, CommonErrorCode.REQUIRED)
            "Email", "Pattern" -> FieldViolation(field, CommonErrorCode.INVALID_FORMAT)
            "Size" -> {
                val min = attrs["min"] as Int
                val len = (rejectedValue as? CharSequence)?.length ?: 0
                if (len < min) {
                    FieldViolation(field, CommonErrorCode.TOO_SHORT, mapOf("minLength" to min))
                } else {
                    FieldViolation(field, CommonErrorCode.TOO_LONG, mapOf("maxLength" to attrs["max"]!!))
                }
            }
            "Min" -> FieldViolation(field, CommonErrorCode.TOO_SMALL, mapOf("min" to attrs["value"]!!))
            "Max" -> FieldViolation(field, CommonErrorCode.TOO_LARGE, mapOf("max" to attrs["value"]!!))
            else -> {
                logger.warn("Unmapped constraint '$code' on field '$field'")
                FieldViolation(field, CommonErrorCode.INVALID)
            }
        }
    }


}