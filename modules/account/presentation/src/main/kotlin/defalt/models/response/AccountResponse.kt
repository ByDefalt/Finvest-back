package defalt.models.response

import defalt.models.dto.BankDto
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * Account of the authenticated user.
 *
 * @property id id of the account.
 * @property bank bank holding the account.
 * @property name name of the account.
 * @property balance current balance.
 * @property currencyCode currency of the account.
 * @property createdAt opening date.
 * @property closedAt closing date, null while the account is open.
 * @property accountStatusCode status of the account.
 * @property description free description.
 * @property accountTypeCode type of the account, which gives the type of its details.
 */
data class AccountResponse(
    @field:NotNull
    val id: Long,
    @field:NotNull
    val bank: BankDto,
    @field:NotNull
    val name: String,
    @field:NotNull
    val balance: BigDecimal,
    @field:NotNull
    val currencyCode: String,
    @field:NotNull
    val createdAt: LocalDateTime,
    val closedAt: LocalDateTime?,
    @field:NotNull
    val accountStatusCode: String,
    val description: String?,
    @field:NotNull
    val accountTypeCode: String,
)
