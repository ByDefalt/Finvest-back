package defalt.models.response

import defalt.models.dto.AccountDetailsDto
import jakarta.validation.constraints.NotNull

/**
 * Account with its details and owners, as listed by `GET /accounts`.
 *
 * @property account the account and its bank.
 * @property details the data specific to the account type.
 * @property owners the owners with their identity.
 */
data class AccountWithDetailsResponse(
    @field:NotNull
    val account: AccountResponse,
    @field:NotNull
    val details: AccountDetailsDto,
    @field:NotNull
    val owners: List<AccountOwnerWithIdentityResponse>,
)
