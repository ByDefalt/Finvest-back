package com.example.finvest.auth.exeption

import com.example.finvest.common.exeption.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import com.example.finvest.common.logger.Logger

@RestControllerAdvice
class AuthExceptionHandler(
    val logger: Logger
) {

    @ExceptionHandler(UserAlreadyExistsException::class)
    fun handleUserAlreadyExists(
        exception: UserAlreadyExistsException
    ): ResponseEntity<ErrorResponse> {
        logger.error(exception.localizedMessage)
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(
                ErrorResponse(
                    status = 409,
                    message = exception.message ?: "User already exists"
                )
            )
    }

    @ExceptionHandler(UserCreationException::class)
    fun handleUserCreation(
        exception: UserCreationException
    ): ResponseEntity<ErrorResponse> {
        logger.error(exception.localizedMessage)
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                ErrorResponse(
                    status = 500,
                    message = exception.message ?: "Failed to create user"
                )
            )
    }
}