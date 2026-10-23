package defalt.models.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.PastOrPresent
import jakarta.validation.constraints.Size
import java.time.LocalDate

/**
 * Details of a PEE (`PEE`).
 *
 * @property openingDate opening date, not in the future.
 * @property employer employer offering the plan.
 */
data class PeeDetailsDto(
    @field:NotNull
    override val accountId: Long,
    @field:PastOrPresent
    @field:NotNull
    val openingDate: LocalDate,
    @field:NotBlank
    @field:Size(max = 255)
    val employer: String,
) : AccountDetailsDto
