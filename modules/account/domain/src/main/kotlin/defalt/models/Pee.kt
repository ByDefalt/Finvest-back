package defalt.models

import defalt.valueobject.AccountId
import java.time.LocalDate

/**
 * Details of a PEE (plan d'épargne entreprise, employee savings plan).
 *
 * @property openingDate opening date of the plan.
 * @property employer employer offering the plan.
 */
data class Pee(
    override val accountId: AccountId,
    val openingDate: LocalDate,
    val employer: String,
) : AccountDetails
