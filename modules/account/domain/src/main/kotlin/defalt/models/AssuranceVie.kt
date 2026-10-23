package defalt.models

import defalt.valueobject.AccountId
import defalt.valueobject.ManagementTypeCode
import java.time.LocalDate

/**
 * Details of a life insurance contract (assurance vie).
 *
 * @property contractNumber number of the contract at the insurer.
 * @property openingDate subscription date of the contract.
 * @property managementTypeCode how the contract is managed (`FREE_MANAGEMENT`, `MANAGED`...).
 */
data class AssuranceVie(
    override val accountId: AccountId,
    val contractNumber: String,
    val openingDate: LocalDate,
    val managementTypeCode: ManagementTypeCode,
) : AccountDetails
