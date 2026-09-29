package com.example.finvest.modules.account.presentation.controller

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.application.usecase.create.CreateAssuranceVieUseCase
import com.example.finvest.modules.account.application.usecase.delete.DeleteAssuranceVieUseCase
import com.example.finvest.modules.account.application.usecase.get.GetAssuranceVieByUserIdUseCase
import com.example.finvest.modules.account.application.usecase.update.UpdateAssuranceVieUseCase
import com.example.finvest.modules.account.domain.cache.ReferenceDataCache
import com.example.finvest.modules.account.presentation.dto.request.AssuranceVieRequest
import com.example.finvest.modules.account.presentation.dto.response.AccountIdResponse
import com.example.finvest.modules.account.presentation.dto.response.AccountWithDetailsDto
import com.example.finvest.modules.account.presentation.dto.response.AssuranceVieDto
import com.example.finvest.modules.account.presentation.mapper.toAccountId
import com.example.finvest.modules.account.presentation.mapper.toAccountIdResponse
import com.example.finvest.modules.account.presentation.mapper.toAssuranceVieWithDetails
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
@RequestMapping("/accounts/assurance-vie")
@AuthenticationRequired
class AssuranceVieController(
    private val get: GetAssuranceVieByUserIdUseCase,
    private val create: CreateAssuranceVieUseCase,
    private val update: UpdateAssuranceVieUseCase,
    private val delete: DeleteAssuranceVieUseCase,
    private val referenceDataCache: ReferenceDataCache,
    private val logger: Logger,
) {
    @GetMapping
    fun get(
        @CurrentUser user: AuthenticatedUser,
    ): List<AssuranceVieDto> {
        logger.info("start get assurance vie accounts for user=${user.id}")
        val result = get(user.id).map { it.toDto() }
        logger.info("finis get assurance vie accounts for user=${user.id}, count=${result.size}")
        return result
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @RequestBody request: AssuranceVieRequest,
        @CurrentUser user: AuthenticatedUser,
    ): AccountIdResponse {
        logger.info("start create assurance vie account for user=${user.id}")
        val result = create(request.toAssuranceVieWithDetails(referenceDataCache)).value.toAccountIdResponse()
        logger.info("finis create assurance vie account for user=${user.id}, accountId=${result.accountId}")
        return result
    }

    @PutMapping
    fun update(
        @RequestBody request: AssuranceVieRequest,
        @CurrentUser user: AuthenticatedUser,
    ): AccountWithDetailsDto {
        logger.info("start update assurance vie account for user=${user.id}")
        val result = update(request.toAssuranceVieWithDetails(referenceDataCache)).toDto(referenceDataCache)
        logger.info("finis update assurance vie account for user=${user.id}, accountId=${result.account.accountId}")
        return result
    }

    @DeleteMapping("/{accountId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(
        @PathVariable accountId: Long,
        @CurrentUser user: AuthenticatedUser,
    ) {
        logger.info("start delete assurance vie account for user=${user.id}, accountId=$accountId")
        delete(accountId.toAccountId())
        logger.info("finis delete assurance vie account for user=${user.id}, accountId=$accountId")
    }
}
