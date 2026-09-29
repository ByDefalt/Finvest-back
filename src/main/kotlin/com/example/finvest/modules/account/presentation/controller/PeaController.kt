package com.example.finvest.modules.account.presentation.controller

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.application.usecase.create.CreatePeaUseCase
import com.example.finvest.modules.account.application.usecase.delete.DeletePeaUseCase
import com.example.finvest.modules.account.application.usecase.get.GetPeaByUserIdUseCase
import com.example.finvest.modules.account.application.usecase.update.UpdatePeaUseCase
import com.example.finvest.modules.account.presentation.dto.request.PeaRequest
import com.example.finvest.modules.account.presentation.dto.response.AccountIdResponse
import com.example.finvest.modules.account.presentation.dto.response.AccountWithDetailsDto
import com.example.finvest.modules.account.presentation.dto.response.PeaDto
import com.example.finvest.modules.account.presentation.mapper.toAccountId
import com.example.finvest.modules.account.presentation.mapper.toAccountIdResponse
import com.example.finvest.modules.account.presentation.mapper.toDto
import com.example.finvest.modules.account.presentation.mapper.toPeaWithDetails
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
@RequestMapping("/accounts/pea")
@AuthenticationRequired
class PeaController(
    private val get: GetPeaByUserIdUseCase,
    private val create: CreatePeaUseCase,
    private val update: UpdatePeaUseCase,
    private val delete: DeletePeaUseCase,
    private val logger: Logger,
) {
    @GetMapping
    fun get(
        @CurrentUser user: AuthenticatedUser,
    ): List<PeaDto> {
        logger.info("start get pea accounts for user=${user.id}")
        val result = get(user.id).map { it.toDto() }
        logger.info("finis get pea accounts for user=${user.id}, count=${result.size}")
        return result
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @RequestBody request: PeaRequest,
        @CurrentUser user: AuthenticatedUser,
    ): AccountIdResponse {
        logger.info("start create pea account for user=${user.id}")
        val result = create(request.toPeaWithDetails()).value.toAccountIdResponse()
        logger.info("finis create pea account for user=${user.id}, accountId=${result.accountId}")
        return result
    }

    @PutMapping
    fun update(
        @RequestBody request: PeaRequest,
        @CurrentUser user: AuthenticatedUser,
    ): AccountWithDetailsDto {
        logger.info("start update pea account for user=${user.id}")
        val result = update(request.toPeaWithDetails()).toDto()
        logger.info("finis update pea account for user=${user.id}, accountId=${result.account.accountId}")
        return result
    }

    @DeleteMapping("/{accountId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(
        @PathVariable accountId: Long,
        @CurrentUser user: AuthenticatedUser,
    ) {
        logger.info("start delete pea account for user=${user.id}, accountId=$accountId")
        delete(accountId.toAccountId())
        logger.info("finis delete pea account for user=${user.id}, accountId=$accountId")
    }
}
