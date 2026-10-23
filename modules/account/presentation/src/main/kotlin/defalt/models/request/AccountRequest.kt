package defalt.models.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * Fields of an account to create or update.
 *
 * @property id id of the account; ignored (on update, the id of the URL is used).
 * @property bankBic BIC of the bank holding the account.
 * @property name name of the account, at most 255 characters.
 * @property balance current balance.
 * @property currencyCode currency of the account (`EUR`...).
 * @property createdAt opening date; ignored on update.
 * @property closedAt closing date, null while the account is open.
 * @property accountStatusCode status of the account (`ACTIVE`, `CLOSED`, `BLOCKED`).
 * @property description free description, at most 1000 characters.
 */
data class AccountRequest(
    @field:NotNull
    val id: Long,
    @field:NotBlank
    val bankBic: String,
    @field:NotBlank
    @field:Size(max = 255)
    val name: String,
    @field:NotNull
    val balance: BigDecimal,
    @field:NotBlank
    val currencyCode: String,
    @field:NotNull
    val createdAt: LocalDateTime,
    val closedAt: LocalDateTime?,
    @field:NotBlank
    val accountStatusCode: String,
    @field:Size(max = 1000)
    val description: String?,
)
