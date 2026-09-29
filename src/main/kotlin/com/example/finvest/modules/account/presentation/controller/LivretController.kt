package com.example.finvest.modules.account.presentation.controller

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.application.usecase.create.CreateLivretUseCase
import com.example.finvest.modules.account.application.usecase.delete.DeleteLivretUseCase
import com.example.finvest.modules.account.application.usecase.get.GetLivretByUserIdUseCase
import com.example.finvest.modules.account.application.usecase.update.UpdateLivretUseCase
import com.example.finvest.modules.account.domain.cache.ReferenceDataCache
import com.example.finvest.modules.account.presentation.dto.request.LivretRequest
import com.example.finvest.modules.account.presentation.dto.response.AccountIdResponse
import com.example.finvest.modules.account.presentation.dto.response.AccountWithDetailsDto
import com.example.finvest.modules.account.presentation.dto.response.LivretDto
import com.example.finvest.modules.account.presentation.mapper.toAccountId
import com.example.finvest.modules.account.presentation.mapper.toAccountIdResponse
import com.example.finvest.modules.account.presentation.mapper.toDto
import com.example.finvest.modules.account.presentation.mapper.toLivretWithDetails
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
@RequestMapping("/accounts/livret")
@AuthenticationRequired
class LivretController(
    private val get: GetLivretByUserIdUseCase,
    private val create: CreateLivretUseCase,
    private val update: UpdateLivretUseCase,
    private val delete: DeleteLivretUseCase,
    private val referenceDataCache: ReferenceDataCache,
    private val logger: Logger,
) {
    @GetMapping
    fun get(
        @CurrentUser user: AuthenticatedUser,
    ): List<LivretDto> {
        logger.info("start get livret accounts for user=${user.id}")
        val result = get(user.id).map { it.toDto() }
        logger.info("finis get livret accounts for user=${user.id}, count=${result.size}")
        return result
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @RequestBody request: LivretRequest,
        @CurrentUser user: AuthenticatedUser,
    ): AccountIdResponse {
        logger.info("start create livret account for user=${user.id}")
        val result = create(request.toLivretWithDetails(referenceDataCache)).value.toAccountIdResponse()
        logger.info("finis create livret account for user=${user.id}, accountId=${result.accountId}")
        return result
    }

    @PutMapping
    fun update(
        @RequestBody request: LivretRequest,
        @CurrentUser user: AuthenticatedUser,
    ): AccountWithDetailsDto {
        logger.info("start update livret account for user=${user.id}")
        val result = update(request.toLivretWithDetails(referenceDataCache)).toDto(referenceDataCache)
        logger.info("finis update livret account for user=${user.id}, accountId=${result.account.accountId}")
        return result
    }

    @DeleteMapping("/{accountId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(
        @PathVariable accountId: Long,
        @CurrentUser user: AuthenticatedUser,
    ) {
        logger.info("start delete livret account for user=${user.id}, accountId=$accountId")
        delete(accountId.toAccountId())
        logger.info("finis delete livret account for user=${user.id}, accountId=$accountId")
    }
}
