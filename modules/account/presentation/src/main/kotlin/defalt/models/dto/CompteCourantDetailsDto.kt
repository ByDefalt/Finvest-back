package defalt.models.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.PositiveOrZero
import jakarta.validation.constraints.Size
import java.math.BigDecimal

/**
 * Details of a current account (`COMPTE_COURANT`).
 *
 * @property iban IBAN of the account, unique.
 * @property bic BIC of the account.
 * @property accountNumber number of the account at its bank.
 * @property overdraftLimit authorized overdraft, positive or zero.
 * @property holderName name of the holder.
 */
data class CompteCourantDetailsDto(
    @field:NotNull
    override val accountId: Long,
    @field:NotBlank
    @field:Size(max = 50)
    val iban: String,
    @field:NotBlank
    @field:Size(max = 50)
    val bic: String,
    @field:NotBlank
    @field:Size(max = 100)
    val accountNumber: String,
    @field:PositiveOrZero
    @field:NotNull
    val overdraftLimit: BigDecimal,
    @field:NotBlank
    @field:Size(max = 255)
    val holderName: String,
) : AccountDetailsDto
