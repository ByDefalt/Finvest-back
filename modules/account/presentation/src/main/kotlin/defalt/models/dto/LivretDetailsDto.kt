package defalt.models.dto

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.PositiveOrZero
import java.math.BigDecimal

/**
 * Details of a regulated savings account (`LIVRET`).
 *
 * @property interestRate annual interest rate in percent, positive or zero.
 * @property ceiling maximum amount that can be deposited, positive.
 */
data class LivretDetailsDto(
    @field:NotNull
    override val accountId: Long,
    @field:PositiveOrZero
    @field:NotNull
    val interestRate: BigDecimal,
    @field:Positive
    @field:NotNull
    val ceiling: BigDecimal,
) : AccountDetailsDto
