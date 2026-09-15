package com.example.finvest.account.controller

import com.example.finvest.account.dto.DashboardResponse
import com.example.finvest.account.service.DashboardService
import com.example.finvest.auth.annotation.AuthenticationRequired
import com.example.finvest.auth.annotation.CurrentUser
import com.example.finvest.auth.mapper.toDomain
import com.example.finvest.common.dto.AuthenticatedUser
import com.example.finvest.common.logger.Logger
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/dashboard")
class DashboardController(
    private val dashBoardService: DashboardService,
    private val logger: Logger
) {
    @GetMapping("/health")
    fun healthCheck(): String {
        logger.debug("DashBoardController.healthCheck - start")
        return "Dashboard service is running"
    }

    @GetMapping("/")
    //@AuthenticationRequired
    fun getDashboard(

    ): DashboardResponse {
        logger.debug("DashBoardController.getDashboard - start")

        val authenticatedUser: AuthenticatedUser = AuthenticatedUser(id = 1, email = "romain@example.com")
        return dashBoardService.getDashboard(authenticatedUser.toDomain())
    }
}