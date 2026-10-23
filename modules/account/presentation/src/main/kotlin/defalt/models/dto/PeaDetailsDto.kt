package defalt.models.dto

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.PastOrPresent
import jakarta.validation.constraints.Positive
import java.math.BigDecimal
import java.time.LocalDate

/**
 * Details of a PEA (`PEA`).
 *
 * @property openingDate opening date, not in the future.
 * @property depositLimit maximum amount that can be deposited, positive.
 */
data class PeaDetailsDto(
    @field:NotNull
    override val accountId: Long,
    @field:PastOrPresent
    @field:NotNull
    val openingDate: LocalDate,
    @field:Positive
    @field:NotNull
    val depositLimit: BigDecimal,
) : AccountDetailsDto
