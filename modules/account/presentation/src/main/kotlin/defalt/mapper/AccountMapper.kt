package defalt.mapper

import defalt.command.CreateAccountCommand
import defalt.command.UpdateAccountCommand
import defalt.models.Account
import defalt.models.AccountDetails
import defalt.models.AccountWithBank
import defalt.models.AccountWithDetails
import defalt.models.Bank
import defalt.models.NewAccountOwner
import defalt.models.dto.BankDto
import defalt.models.request.AccountOwnerRequest
import defalt.models.request.AccountRequest
import defalt.models.request.AccountWithDetailsRequest
import defalt.models.response.AccountOwnerWithIdentityResponse
import defalt.models.response.AccountResponse
import defalt.models.response.AccountWithDetailsResponse
import defalt.valueobject.AccountId
import defalt.valueobject.AccountStatusCode
import defalt.valueobject.AccountTypeCode
import defalt.valueobject.BankBic
import defalt.valueobject.CurrencyCode
import defalt.valueobject.Email

/**
 * API representation of the account with its details and owners.
 *
 * @receiver the account with its details and owners.
 * @param detailsMapperResolver maps the account details between the domain and the API.
 * @return the response of the account.
 */
fun <T : AccountDetails> AccountWithDetails<T>.toResponse(
    detailsMapperResolver: AccountDetailsMapperResolver,
): AccountWithDetailsResponse =
    AccountWithDetailsResponse(
        account = account.toResponse(),
        details = detailsMapperResolver.toDto(account.account.accountTypeCode, details),
        owners =
            owners.map { owner ->
                AccountOwnerWithIdentityResponse(
                    ownershipPercentage = owner.accountOwner.ownershipPercentage,
                    firstName = owner.identity.firstName,
                    lastName = owner.identity.lastName,
                    email = owner.identity.email.value,
                )
            },
    )

/**
 * API representation of the account with its bank.
 *
 * @receiver the account with its bank.
 * @return the response of the account.
 */
fun AccountWithBank.toResponse(): AccountResponse =
    AccountResponse(
        id = account.id.value,
        bank = bank.toDto(),
        name = account.name,
        balance = account.balance,
        currencyCode = account.currencyCode.value,
        createdAt = account.createdAt,
        closedAt = account.closedAt,
        accountStatusCode = account.accountStatusCode.value,
        description = account.description,
        accountTypeCode = account.accountTypeCode.value,
    )

/**
 * API representation of the bank.
 *
 * @receiver the bank.
 * @return the DTO of the bank.
 */
fun Bank.toDto(): BankDto =
    BankDto(
        bic = this.bic.value,
        name = this.name,
        logo = this.logo,
    )

/**
 * Domain account of the request.
 *
 * @receiver the fields of the account.
 * @param accountTypeCode type of the account, sent at the root of the request body.
 * @return the domain account.
 */
fun AccountRequest.toDomain(accountTypeCode: AccountTypeCode): Account =
    Account(
        id = AccountId(this.id),
        bankBic = BankBic(this.bankBic),
        name = this.name,
        balance = this.balance,
        currencyCode = CurrencyCode(this.currencyCode),
        createdAt = this.createdAt,
        closedAt = this.closedAt,
        accountStatusCode = AccountStatusCode(this.accountStatusCode),
        description = this.description,
        accountTypeCode = accountTypeCode,
    )

/**
 * Owner to resolve from its email.
 *
 * @receiver the owner of the request.
 * @return the owner to resolve.
 */
fun AccountOwnerRequest.toDomain(): NewAccountOwner =
    NewAccountOwner(
        email = Email(this.email),
        ownershipPercentage = this.ownershipPercentage,
    )

/**
 * Command creating the account of the request.
 *
 * @receiver the body of the creation.
 * @param detailsMapperResolver maps the account details between the domain and the API.
 * @return the command creating the account.
 */
fun AccountWithDetailsRequest.toCommand(detailsMapperResolver: AccountDetailsMapperResolver): CreateAccountCommand {
    val account = this.account.toDomain(AccountTypeCode(accountTypeCode))
    return CreateAccountCommand(
        account = account,
        details = detailsMapperResolver.toDomain(account.accountTypeCode, this.details),
        owners = this.owners.map { it.toDomain() },
    )
}

/**
 * Command updating the account [accountId] with the request.
 *
 * @receiver the body of the update.
 * @param accountId id of the account, taken from the URL: the id of the body is ignored.
 * @param detailsMapperResolver maps the account details between the domain and the API.
 * @return the command updating the account.
 */
fun AccountWithDetailsRequest.toUpdateCommand(
    accountId: Long,
    detailsMapperResolver: AccountDetailsMapperResolver,
): UpdateAccountCommand {
    val account = this.account.toDomain(AccountTypeCode(accountTypeCode)).copy(id = AccountId(accountId))
    return UpdateAccountCommand(
        account = account,
        details = detailsMapperResolver.toDomain(account.accountTypeCode, this.details),
        owners = this.owners.map { it.toDomain() },
    )
}
