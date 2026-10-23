package defalt.models

import defalt.valueobject.AccountId
import defalt.valueobject.AccountStatusCode
import defalt.valueobject.AccountTypeCode
import defalt.valueobject.BankBic
import defalt.valueobject.CurrencyCode
import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * Financial account of one or several owners, held in a bank.
 * Its type-specific data are its [AccountDetails].
 *
 * @property id id of the account, ignored when the account is created.
 * @property bankBic BIC of the bank holding the account.
 * @property name name given to the account by its owners.
 * @property balance current balance, in [currencyCode].
 * @property currencyCode currency of the account.
 * @property createdAt opening date of the account.
 * @property closedAt closing date, null while the account is open.
 * @property accountStatusCode status of the account (`ACTIVE`, `CLOSED`...).
 * @property description free description of the account.
 * @property accountTypeCode type of the account (`PEA`, `LIVRET`...), which gives the type of its details.
 */
data class Account(
    val id: AccountId,
    val bankBic: BankBic,
    val name: String,
    val balance: BigDecimal,
    val currencyCode: CurrencyCode,
    val createdAt: LocalDateTime,
    val closedAt: LocalDateTime?,
    val accountStatusCode: AccountStatusCode,
    val description: String?,
    val accountTypeCode: AccountTypeCode,
)
