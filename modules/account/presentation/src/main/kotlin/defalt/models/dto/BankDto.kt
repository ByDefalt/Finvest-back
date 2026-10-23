package defalt.models.dto

import jakarta.validation.constraints.NotNull

/**
 * Bank holding an account.
 *
 * @property bic BIC of the bank.
 * @property name name of the bank.
 * @property logo logo of the bank, if any.
 */
data class BankDto(
    @field:NotNull
    val bic: String,
    @field:NotNull
    val name: String,
    val logo: String?,
)
