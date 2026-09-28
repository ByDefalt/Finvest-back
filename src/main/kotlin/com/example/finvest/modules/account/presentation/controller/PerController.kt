package com.example.finvest.modules.account.presentation.controller

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.application.usecase.CreatePerUseCase
import com.example.finvest.modules.account.application.usecase.DeletePerUseCase
import com.example.finvest.modules.account.application.usecase.GetPerByUserIdUseCase
import com.example.finvest.modules.account.application.usecase.UpdatePerUseCase
import com.example.finvest.modules.account.presentation.dto.AccountIdResponse
import com.example.finvest.modules.account.presentation.dto.PerDto
import com.example.finvest.modules.account.presentation.dto.PerRequest
import com.example.finvest.modules.account.presentation.mapper.toAccountId
import com.example.finvest.modules.account.presentation.mapper.toAccountIdResponse
import com.example.finvest.modules.account.presentation.mapper.toDto
import com.example.finvest.modules.account.presentation.mapper.toPerWithDetails
import com.example.finvest.modules.shared.application.models.AuthenticatedUser
import com.example.finvest.modules.shared.presentation.security.AuthenticationRequired
import com.example.finvest.modules.shared.presentation.security.CurrentUser
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/accounts/per")
@AuthenticationRequired
class PerController(
    private val get: GetPerByUserIdUseCase,
    private val create: CreatePerUseCase,
    private val update: UpdatePerUseCase,
    private val delete: DeletePerUseCase,
    private val logger: Logger,
) {
    @GetMapping
    fun get(
        @CurrentUser user: AuthenticatedUser,
    ): List<PerDto> {
        logger.info("start get per accounts for user=${user.id}")
        val result = get(user.id).map { it.toDto() }
        logger.info("finis get per accounts for user=${user.id}, count=${result.size}")
        return result
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @RequestBody request: PerRequest,
        @CurrentUser user: AuthenticatedUser,
    ): AccountIdResponse {
        logger.info("start create per account for user=${user.id}")
        val result = create(request.toPerWithDetails()).value.toAccountIdResponse()
        logger.info("finis create per account for user=${user.id}, accountId=${result.accountId}")
        return result
    }

    @PutMapping
    fun update(
        @RequestBody request: PerRequest,
        @CurrentUser user: AuthenticatedUser,
    ): PerDto {
        logger.info("start update per account for user=${user.id}")
        val result = update(request.toPerWithDetails()).toDto()
        logger.info("finis update per account for user=${user.id}, accountId=${result.accountId}")
        return result
    }

    @DeleteMapping("/{accountId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(
        @PathVariable accountId: Long,
        @CurrentUser user: AuthenticatedUser,
    ) {
        logger.info("start delete per account for user=${user.id}, accountId=$accountId")
        delete(accountId.toAccountId())
        logger.info("finis delete per account for user=${user.id}, accountId=$accountId")
    }
}
