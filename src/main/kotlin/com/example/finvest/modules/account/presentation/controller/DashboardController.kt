package com.example.finvest.modules.account.presentation.controller

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.application.usecase.get.GetDashboardUseCase
import com.example.finvest.modules.account.presentation.dto.response.AccountWithDetailsDto
import com.example.finvest.modules.account.presentation.mapper.detailsmapper.DetailsDtoMapper
import com.example.finvest.modules.shared.application.models.AuthenticatedUser
import com.example.finvest.modules.shared.presentation.security.CurrentUser
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/dashboard")
class DashboardController(
    private val get: GetDashboardUseCase,
    private val detailsDtoMapper: List<DetailsDtoMapper>,
    private val logger: Logger,
) {
    @GetMapping("/")
    fun getDashboard(
        //@CurrentUser authenticatedUser: AuthenticatedUser,
    ): List<AccountWithDetailsDto> {
        val authenticatedUser = AuthenticatedUser(id = 1, email = "testuser") // Mocked authenticated user for demonstration
        logger.info("Fetching dashboard data")
        val dashboardData = get(authenticatedUser.id)
        val mappedData =
            dashboardData.mapNotNull { accountWithDetails ->
                detailsDtoMapper.firstOrNull { it.mapToDto(accountWithDetails) != null }?.mapToDto(accountWithDetails)
            }
        logger.info("Dashboard data fetched successfully")
        return mappedData
    }
}
