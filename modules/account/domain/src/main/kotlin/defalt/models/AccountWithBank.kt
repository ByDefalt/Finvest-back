package defalt.models

/**
 * Account with the bank holding it.
 *
 * @property account the account.
 * @property bank the bank of [Account.bankBic].
 */
data class AccountWithBank(
    val account: Account,
    val bank: Bank,
)
