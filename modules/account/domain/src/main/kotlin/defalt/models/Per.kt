package defalt.models

import defalt.valueobject.AccountId
import defalt.valueobject.ManagementTypeCode
import java.time.LocalDate

/**
 * Details of a PER (plan d'épargne retraite, retirement savings plan).
 *
 * @property contractNumber number of the contract.
 * @property openingDate subscription date of the contract.
 * @property managementTypeCode how the contract is managed (`FREE_MANAGEMENT`, `MANAGED`...).
 */
data class Per(
    override val accountId: AccountId,
    val contractNumber: String,
    val openingDate: LocalDate,
    val managementTypeCode: ManagementTypeCode,
) : AccountDetails
