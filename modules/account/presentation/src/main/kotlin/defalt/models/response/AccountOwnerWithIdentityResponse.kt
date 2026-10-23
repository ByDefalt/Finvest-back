package defalt.models.response

import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

/**
 * Owner of an account.
 *
 * @property ownershipPercentage owned share of the account, in percent.
 * @property firstName first name of the owner.
 * @property lastName last name of the owner.
 * @property email email of the owner.
 */
data class AccountOwnerWithIdentityResponse(
    @field:NotNull
    val ownershipPercentage: BigDecimal,
    @field:NotNull
    val firstName: String,
    @field:NotNull
    val lastName: String,
    @field:NotNull
    val email: String,
)
