package defalt.models.request

import com.fasterxml.jackson.annotation.JsonTypeInfo
import defalt.models.dto.AccountDetailsDto
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

/**
 * Body of the creation and update of an account:
 *
 * ```json
 * {
 *   "accountTypeCode": "PEA",
 *   "account": { "bankBic": "FTNOFRP1", "name": "Mon PEA", ... },
 *   "details": { "accountId": 0, "openingDate": "2022-03-15", "depositLimit": 150000 },
 *   "owners": [ { "email": "romain@example.com", "ownershipPercentage": 100 } ]
 * }
 * ```
 *
 * @property accountTypeCode type of the account; also selects the DTO of [details].
 * @property account fields of the account.
 * @property details details of the account, of the DTO registered for [accountTypeCode].
 * @property owners owners of the account, at least one.
 */
data class AccountWithDetailsRequest(
    @field:NotBlank
    val accountTypeCode: String,
    @field:NotNull
    @field:Valid
    val account: AccountRequest,
    @field:Valid
    @param:JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
        property = "accountTypeCode",
        visible = true,
    )
    @field:NotNull
    val details: AccountDetailsDto,
    @field:NotEmpty
    @field:Valid
    val owners: List<AccountOwnerRequest>,
)
