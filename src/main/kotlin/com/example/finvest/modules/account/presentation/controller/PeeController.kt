package com.example.finvest.modules.account.presentation.controller

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.application.usecase.CreatePeeUseCase
import com.example.finvest.modules.account.application.usecase.DeletePeeUseCase
import com.example.finvest.modules.account.application.usecase.GetPeeByUserIdUseCase
import com.example.finvest.modules.account.application.usecase.UpdatePeeUseCase
import com.example.finvest.modules.account.presentation.dto.AccountIdResponse
import com.example.finvest.modules.account.presentation.dto.PeeDto
import com.example.finvest.modules.account.presentation.dto.PeeRequest
import com.example.finvest.modules.account.presentation.mapper.toAccountId
import com.example.finvest.modules.account.presentation.mapper.toAccountIdResponse
import com.example.finvest.modules.account.presentation.mapper.toDto
import com.example.finvest.modules.account.presentation.mapper.toPeeWithDetails
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
@RequestMapping("/accounts/pee")
@AuthenticationRequired
class PeeController(
    private val get: GetPeeByUserIdUseCase,
    private val create: CreatePeeUseCase,
    private val update: UpdatePeeUseCase,
    private val delete: DeletePeeUseCase,
    private val logger: Logger,
) {
    @GetMapping
    fun get(
        @CurrentUser user: AuthenticatedUser,
    ): List<PeeDto> {
        logger.info("start get pee accounts for user=${user.id}")
        val result = get(user.id).map { it.toDto() }
        logger.info("finis get pee accounts for user=${user.id}, count=${result.size}")
        return result
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @RequestBody request: PeeRequest,
        @CurrentUser user: AuthenticatedUser,
    ): AccountIdResponse {
        logger.info("start create pee account for user=${user.id}")
        val result = create(request.toPeeWithDetails()).value.toAccountIdResponse()
        logger.info("finis create pee account for user=${user.id}, accountId=${result.accountId}")
        return result
    }

    @PutMapping
    fun update(
        @RequestBody request: PeeRequest,
        @CurrentUser user: AuthenticatedUser,
    ): PeeDto {
        logger.info("start update pee account for user=${user.id}")
        val result = update(request.toPeeWithDetails()).toDto()
        logger.info("finis update pee account for user=${user.id}, accountId=${result.accountId}")
        return result
    }

    @DeleteMapping("/{accountId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(
        @PathVariable accountId: Long,
        @CurrentUser user: AuthenticatedUser,
    ) {
        logger.info("start delete pee account for user=${user.id}, accountId=$accountId")
        delete(accountId.toAccountId())
        logger.info("finis delete pee account for user=${user.id}, accountId=$accountId")
    }
}
