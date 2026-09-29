package com.example.finvest.modules.account.presentation.controller

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.application.usecase.create.CreateCompteTitreUseCase
import com.example.finvest.modules.account.application.usecase.delete.DeleteCompteTitreUseCase
import com.example.finvest.modules.account.application.usecase.get.GetCompteTitreByUserIdUseCase
import com.example.finvest.modules.account.application.usecase.update.UpdateCompteTitreUseCase
import com.example.finvest.modules.account.domain.cache.ReferenceDataCache
import com.example.finvest.modules.account.presentation.dto.request.CompteTitreRequest
import com.example.finvest.modules.account.presentation.dto.response.AccountIdResponse
import com.example.finvest.modules.account.presentation.dto.response.AccountWithDetailsDto
import com.example.finvest.modules.account.presentation.dto.response.CompteTitreDto
import com.example.finvest.modules.account.presentation.mapper.toAccountId
import com.example.finvest.modules.account.presentation.mapper.toAccountIdResponse
import com.example.finvest.modules.account.presentation.mapper.toCompteTitreWithDetails
import com.example.finvest.modules.account.presentation.mapper.toDto
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
@RequestMapping("/accounts/compte-titre")
@AuthenticationRequired
class CompteTitreController(
    private val get: GetCompteTitreByUserIdUseCase,
    private val create: CreateCompteTitreUseCase,
    private val update: UpdateCompteTitreUseCase,
    private val delete: DeleteCompteTitreUseCase,
    private val referenceDataCache: ReferenceDataCache,
    private val logger: Logger,
) {
    @GetMapping
    fun get(
        @CurrentUser user: AuthenticatedUser,
    ): List<CompteTitreDto> {
        logger.info("start get compte titre accounts for user=${user.id}")
        val result = get(user.id).map { it.toDto() }
        logger.info("finis get compte titre accounts for user=${user.id}, count=${result.size}")
        return result
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @RequestBody request: CompteTitreRequest,
        @CurrentUser user: AuthenticatedUser,
    ): AccountIdResponse {
        logger.info("start create compte titre account for user=${user.id}")
        val result = create(request.toCompteTitreWithDetails(referenceDataCache)).value.toAccountIdResponse()
        logger.info("finis create compte titre account for user=${user.id}, accountId=${result.accountId}")
        return result
    }

    @PutMapping
    fun update(
        @RequestBody request: CompteTitreRequest,
        @CurrentUser user: AuthenticatedUser,
    ): AccountWithDetailsDto {
        logger.info("start update compte titre account for user=${user.id}")
        val result = update(request.toCompteTitreWithDetails(referenceDataCache)).toDto(referenceDataCache)
        logger.info("finis update compte titre account for user=${user.id}, accountId=${result.account.accountId}")
        return result
    }

    @DeleteMapping("/{accountId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(
        @PathVariable accountId: Long,
        @CurrentUser user: AuthenticatedUser,
    ) {
        logger.info("start delete compte titre account for user=${user.id}, accountId=$accountId")
        delete(accountId.toAccountId())
        logger.info("finis delete compte titre account for user=${user.id}, accountId=$accountId")
    }
}
