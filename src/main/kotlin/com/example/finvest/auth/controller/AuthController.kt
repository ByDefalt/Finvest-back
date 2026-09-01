package com.example.finvest.auth.controller

import com.example.finvest.common.logger.Logger
import com.example.finvest.auth.dto.RegisterUserRequest
import com.example.finvest.auth.dto.LoginRequest
import com.example.finvest.auth.dto.LoginResponse
import com.example.finvest.auth.dto.UserResponse
import com.example.finvest.auth.mapper.toDto
import com.example.finvest.auth.service.AuthService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService,
    private val logger: Logger
) {

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    fun register(@Valid @RequestBody user: RegisterUserRequest): UserResponse {
        logger.debug("AuthController.register - start for email=${user.email}")
        val createdUser = authService.register(user.email, user.password)
        logger.info("AuthController.register - success id=${createdUser.id} email=${createdUser.email}")
        return createdUser.toDto()
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody loginRequest: LoginRequest): LoginResponse {
        logger.debug("AuthController.login - start for email=${loginRequest.email}")
        val token = authService.login(loginRequest.email, loginRequest.password)
        logger.info("AuthController.login - success")
        return LoginResponse(token.value)
    }

    @GetMapping("/health")
    fun healthCheck(): String {
        logger.debug("AuthController.healthCheck - start")
        return "Auth service is running"
    }
}