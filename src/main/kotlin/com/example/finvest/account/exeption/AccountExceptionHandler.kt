package com.example.finvest.account.exeption

import com.example.finvest.common.exeption.ErrorResponse
import com.example.finvest.common.logger.Logger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.security.auth.login.AccountNotFoundException

@RestControllerAdvice
class AccountExceptionHandler(
    val logger: Logger
) {
    @ExceptionHandler(AccountNotFoundException::class)
    fun handleAccountNotFound(
        exception: AccountNotFoundException
    ): ResponseEntity<ErrorResponse> {
        logger.error(exception.localizedMessage)
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(
                ErrorResponse(
                    status = 404,
                    message = exception.message ?: "Account not found"
                )
            )
    }

}