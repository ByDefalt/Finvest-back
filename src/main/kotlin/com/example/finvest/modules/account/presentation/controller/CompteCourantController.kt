package com.example.finvest.modules.account.presentation.controller

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.application.usecase.CreateCompteCourantUseCase
import com.example.finvest.modules.account.application.usecase.DeleteCompteCourantUseCase
import com.example.finvest.modules.account.application.usecase.GetCompteCourantByUserIdUseCase
import com.example.finvest.modules.account.application.usecase.UpdateCompteCourantUseCase
import com.example.finvest.modules.account.presentation.dto.AccountIdResponse
import com.example.finvest.modules.account.presentation.dto.CompteCourantDto
import com.example.finvest.modules.account.presentation.dto.CompteCourantRequest
import com.example.finvest.modules.account.presentation.mapper.toAccountId
import com.example.finvest.modules.account.presentation.mapper.toAccountIdResponse
import com.example.finvest.modules.account.presentation.mapper.toCompteCourantWithDetails
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
@RequestMapping("/accounts/compte-courant")
@AuthenticationRequired
class CompteCourantController(
    private val get: GetCompteCourantByUserIdUseCase,
    private val create: CreateCompteCourantUseCase,
    private val update: UpdateCompteCourantUseCase,
    private val delete: DeleteCompteCourantUseCase,
    private val logger: Logger,
) {
    @GetMapping
    fun get(
        @CurrentUser user: AuthenticatedUser,
    ): List<CompteCourantDto> {
        logger.info("start get compte courant accounts for user=${user.id}")
        val result = get(user.id).map { it.toDto() }
        logger.info("finis get compte courant accounts for user=${user.id}, count=${result.size}")
        return result
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @RequestBody request: CompteCourantRequest,
        @CurrentUser user: AuthenticatedUser,
    ): AccountIdResponse {
        logger.info("start create compte courant account for user=${user.id}")
        val result = create(request.toCompteCourantWithDetails()).value.toAccountIdResponse()
        logger.info("finis create compte courant account for user=${user.id}, accountId=${result.accountId}")
        return result
    }

    @PutMapping
    fun update(
        @RequestBody request: CompteCourantRequest,
        @CurrentUser user: AuthenticatedUser,
    ): CompteCourantDto {
        logger.info("start update compte courant account for user=${user.id}")
        val result = update(request.toCompteCourantWithDetails()).toDto()
        logger.info("finis update compte courant account for user=${user.id}, accountId=${result.accountId}")
        return result
    }

    @DeleteMapping("/{accountId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(
        @PathVariable accountId: Long,
        @CurrentUser user: AuthenticatedUser,
    ) {
        logger.info("start delete compte courant account for user=${user.id}, accountId=$accountId")
        delete(accountId.toAccountId())
        logger.info("finis delete compte courant account for user=${user.id}, accountId=$accountId")
    }
}
