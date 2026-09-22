package com.example.finvest.modules.account.presentation.controller

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.application.usecase.GetDashboardUseCase
import com.example.finvest.modules.account.presentation.dto.DashboardResponse
import com.example.finvest.modules.account.presentation.mapper.toDashboardResponse
import com.example.finvest.modules.auth.presentation.security.AuthenticationRequired
import com.example.finvest.modules.auth.presentation.security.CurrentUser
import com.example.finvest.modules.shared.application.security.AuthenticatedUser
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/dashboard")
class DashboardController(
    private val getDashboardUseCase: GetDashboardUseCase,
    private val logger: Logger
) {

    @GetMapping("/")
    @AuthenticationRequired
    fun getDashboard(
        @CurrentUser authenticatedUser: AuthenticatedUser
    ): DashboardResponse {
        logger.info("start")
        return getDashboardUseCase(authenticatedUser).toDashboardResponse(authenticatedUser)
    }
}