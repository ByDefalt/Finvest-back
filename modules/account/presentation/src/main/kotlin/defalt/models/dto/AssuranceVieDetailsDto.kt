package defalt.models.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.PastOrPresent
import jakarta.validation.constraints.Size
import java.time.LocalDate

/**
 * Details of a life insurance contract (`ASSURANCE_VIE`).
 *
 * @property contractNumber number of the contract at the insurer.
 * @property openingDate subscription date, not in the future.
 * @property managementTypeCode management of the contract (`FREE_MANAGEMENT`, `MANAGED`...).
 */
data class AssuranceVieDetailsDto(
    @field:NotNull
    override val accountId: Long,
    @field:NotBlank
    @field:Size(max = 100)
    val contractNumber: String,
    @field:PastOrPresent
    @field:NotNull
    val openingDate: LocalDate,
    @field:NotBlank
    @field:Size(max = 50)
    val managementTypeCode: String,
) : AccountDetailsDto
