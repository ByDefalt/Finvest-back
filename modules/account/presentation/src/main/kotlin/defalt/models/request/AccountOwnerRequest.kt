package defalt.models.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

/**
 * Owner of an account to create or update.
 *
 * @property ownershipPercentage owned share of the account, in percent; all the shares add up to 100.
 * @property email email of an internal or external user.
 */
data class AccountOwnerRequest(
    @field:NotNull
    val ownershipPercentage: BigDecimal,
    @field:NotBlank
    @field:Email
    val email: String,
)
