package defalt.models

import defalt.valueobject.AccountId

/**
 * Details of a securities account (compte-titres).
 *
 * @property accountNumber number of the account at its bank.
 */
data class CompteTitres(
    override val accountId: AccountId,
    val accountNumber: String,
) : AccountDetails
