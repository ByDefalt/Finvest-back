package defalt.models

import defalt.valueobject.BankBic

/**
 * Bank holding accounts.
 *
 * @property bic BIC of the bank, its identifier.
 * @property name name of the bank.
 * @property logo logo of the bank, if any.
 */
data class Bank(
    val bic: BankBic,
    val name: String,
    val logo: String?,
)
