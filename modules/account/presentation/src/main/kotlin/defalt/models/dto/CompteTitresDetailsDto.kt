package defalt.models.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

/**
 * Details of a securities account (`COMPTE_TITRES`).
 *
 * @property accountNumber number of the account at its bank, unique.
 */
data class CompteTitresDetailsDto(
    @field:NotNull
    override val accountId: Long,
    @field:NotBlank
    @field:Size(max = 100)
    val accountNumber: String,
) : AccountDetailsDto
