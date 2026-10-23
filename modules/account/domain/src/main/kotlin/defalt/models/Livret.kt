package defalt.models

import defalt.valueobject.AccountId
import java.math.BigDecimal

/**
 * Details of a regulated savings account (livret).
 *
 * @property interestRate annual interest rate, in percent.
 * @property ceiling maximum amount that can be deposited.
 */
data class Livret(
    override val accountId: AccountId,
    val interestRate: BigDecimal,
    val ceiling: BigDecimal,
) : AccountDetails
