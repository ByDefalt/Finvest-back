package defalt.controller

import defalt.logger.Logger
import defalt.mapper.AccountDetailsMapperResolver
import defalt.mapper.toCommand
import defalt.mapper.toResponse
import defalt.mapper.toUpdateCommand
import defalt.models.AuthenticatedUser
import defalt.models.request.AccountWithDetailsRequest
import defalt.models.response.AccountWithDetailsResponse
import defalt.security.AuthenticationRequired
import defalt.security.CurrentUser
import defalt.usecase.CreateAccountUseCase
import defalt.usecase.DeleteAccountUseCase
import defalt.usecase.GetAccountWithDetailsUseCase
import defalt.usecase.UpdateAccountUseCase
import defalt.valueobject.AccountId
import jakarta.validation.Valid
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

/**
 * REST API of the accounts of the authenticated user.
 *
 * @param getAccountWithDetailsUseCase lists the accounts of a user.
 * @param createAccountUseCase creates an account.
 * @param updateAccountUseCase updates an account.
 * @param deleteAccountUseCase deletes an account.
 * @param detailsMapperResolver maps the account details between the domain and the API.
 * @param logger logger of the class.
 */
@RestController
@RequestMapping("/accounts")
@AuthenticationRequired
class AccountController(
    private val getAccountWithDetailsUseCase: GetAccountWithDetailsUseCase,
    private val createAccountUseCase: CreateAccountUseCase,
    private val updateAccountUseCase: UpdateAccountUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val detailsMapperResolver: AccountDetailsMapperResolver,
    private val logger: Logger,
) {
    /**
     * `GET /accounts`: the accounts owned by the authenticated user, with their bank, details and owners.
     *
     * @param authenticatedUser user authenticated by the access token of the request.
     * @return the accounts of the user, an empty list if there is none.
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAllAccountsWithDetails(
        @CurrentUser authenticatedUser: AuthenticatedUser,
    ): List<AccountWithDetailsResponse> = getAccountWithDetailsUseCase(authenticatedUser).map { it.toResponse(detailsMapperResolver) }

    /**
     * `POST /accounts`: creates an account owned by the authenticated user and the given co-owners.
     * Answers `201 Created`, or an error with a translatable `code` (validation, ownership rules, unknown owner...).
     *
     * @param authenticatedUser user authenticated by the access token of the request.
     * @param accountWithDetailsRequest body of the request: the account, its details and its owners.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createAccount(
        @CurrentUser authenticatedUser: AuthenticatedUser,
        @Valid @RequestBody accountWithDetailsRequest: AccountWithDetailsRequest,
    ) {
        logger.debug("start createAccount for accountType=${accountWithDetailsRequest.accountTypeCode}")
        createAccountUseCase(authenticatedUser, accountWithDetailsRequest.toCommand(detailsMapperResolver))
        logger.info("success createAccount")
    }

    /**
     * `PUT /accounts/{accountId}`: replaces the fields, the details and the owners of an account of the
     * authenticated user. Answers `204 No Content`; the account type cannot change.
     *
     * @param authenticatedUser user authenticated by the access token of the request.
     * @param accountId id of the account to update, from the URL.
     * @param accountWithDetailsRequest body of the request: the account, its details and its owners.
     */
    @PutMapping("/{accountId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updateAccount(
        @CurrentUser authenticatedUser: AuthenticatedUser,
        @PathVariable accountId: Long,
        @Valid @RequestBody accountWithDetailsRequest: AccountWithDetailsRequest,
    ) {
        logger.debug("start updateAccount for accountId=$accountId")
        updateAccountUseCase(authenticatedUser, accountWithDetailsRequest.toUpdateCommand(accountId, detailsMapperResolver))
        logger.info("success updateAccount for accountId=$accountId")
    }

    /**
     * `DELETE /accounts/{accountId}`: deletes an account of the authenticated user, with its details and owners.
     * Answers `204 No Content`.
     *
     * @param authenticatedUser user authenticated by the access token of the request.
     * @param accountId id of the account to delete, from the URL.
     */
    @DeleteMapping("/{accountId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteAccount(
        @CurrentUser authenticatedUser: AuthenticatedUser,
        @PathVariable accountId: Long,
    ) {
        logger.debug("start deleteAccount for accountId=$accountId")
        deleteAccountUseCase(authenticatedUser, AccountId(accountId))
        logger.info("success deleteAccount for accountId=$accountId")
    }
}
