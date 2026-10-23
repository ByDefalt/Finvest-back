package defalt.models

/**
 * Complete view of an account: its bank, its type-specific details and its owners.
 *
 * @param T type of the details.
 * @property account the account and its bank.
 * @property details the data specific to the account type.
 * @property owners the owners with their identity.
 */
data class AccountWithDetails<out T : AccountDetails>(
    val account: AccountWithBank,
    val details: T,
    val owners: List<AccountOwnerWithIdentity>,
)
