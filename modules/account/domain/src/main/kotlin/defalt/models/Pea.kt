package defalt.models

import defalt.valueobject.AccountId
import java.math.BigDecimal
import java.time.LocalDate

/**
 * Details of a PEA (plan d'épargne en actions, French equity savings plan).
 *
 * @property openingDate opening date, which drives the tax advantages.
 * @property depositLimit maximum amount that can be deposited.
 */
data class Pea(
    override val accountId: AccountId,
    val openingDate: LocalDate,
    val depositLimit: BigDecimal,
) : AccountDetails
