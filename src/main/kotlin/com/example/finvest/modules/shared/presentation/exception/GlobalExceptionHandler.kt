package com.example.finvest.modules.shared.presentation.exception

import com.example.finvest.logger.Logger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler(
    val logger: Logger,
) {
    @ExceptionHandler(Exception::class)
    fun handleUnknownException(e: Exception): ResponseEntity<ErrorResponse> {
        logger.error(e.localizedMessage)

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                ErrorResponse(
                    status = 500,
                    message = "An unexpected error occurred",
                ),
            )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(exception: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        logger.error(exception.localizedMessage)

        val fieldErrors =
            exception.bindingResult.fieldErrors
                .associate { it.field to it.defaultMessage }

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                ErrorResponse(
                    status = 400,
                    message = "Field validation error",
                    errors = fieldErrors,
                ),
            )
    }
}
