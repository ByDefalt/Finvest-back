package defalt.models

import defalt.valueobject.AccountId
import java.math.BigDecimal

/**
 * Details of a current account (compte courant).
 *
 * @property iban IBAN of the account.
 * @property bic BIC of the account.
 * @property accountNumber number of the account at its bank.
 * @property overdraftLimit authorized overdraft.
 * @property holderName name of the holder printed on the bank details.
 */
data class CompteCourant(
    override val accountId: AccountId,
    val iban: String,
    val bic: String,
    val accountNumber: String,
    val overdraftLimit: BigDecimal,
    val holderName: String,
) : AccountDetails
