package com.example.finvest.account.controller

import com.example.finvest.account.dto.AccountCreateRequest
import com.example.finvest.account.dto.AccountDeleteRequest
import com.example.finvest.account.dto.AccountResponse
import com.example.finvest.account.dto.AccountUpdateRequest
import com.example.finvest.account.mapper.toDto
import com.example.finvest.account.service.AccountService
import com.example.finvest.auth.annotation.AuthenticationRequired
import com.example.finvest.auth.annotation.CurrentUser
import com.example.finvest.common.dto.AuthenticatedUser
import com.example.finvest.common.logger.Logger
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/accounts")
class AccountController(
    private val accountService: AccountService,
    private val logger: Logger
) {

    @GetMapping
    @AuthenticationRequired
    fun getAllMyAccounts(
        @CurrentUser authenticatedUser: AuthenticatedUser
    ): List<AccountResponse> {
        logger.info("AccountController: Fetching all accounts")
        return accountService.getAllMyAccounts(authenticatedUser.id).toDto()
    }

    @PostMapping
    @AuthenticationRequired
    @ResponseStatus(HttpStatus.CREATED)
    fun createAccount(
        @CurrentUser authenticatedUser: AuthenticatedUser,
        @Valid @RequestBody account: AccountCreateRequest
    ): AccountResponse {
        logger.info("AccountController: Creating new account")
        return accountService.createAccount(
            account,
            authenticatedUser.id
        ).toDto()
    }

    @PutMapping
    @AuthenticationRequired
    fun updateAccount(
        @CurrentUser authenticatedUser: AuthenticatedUser,
        @Valid @RequestBody account: AccountUpdateRequest
    ): AccountResponse {
        logger.info("AccountController: Updating account")
        return accountService.updateAccount(
            account,
            authenticatedUser.id
        ).toDto()
    }

    @DeleteMapping
    @AuthenticationRequired
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteAccount(
        @CurrentUser authenticatedUser: AuthenticatedUser,
        @Valid @RequestBody account: AccountDeleteRequest
    ) {
        logger.info("AccountController: Deleting account")
        accountService.deleteAccount(account.id, authenticatedUser.id)
    }

}
