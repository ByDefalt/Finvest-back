package defalt.models

/**
 * Owner of an account with the identity of the user.
 *
 * @property accountOwner ownership of the account.
 * @property identity name and email of the owner.
 */
data class AccountOwnerWithIdentity(
    val accountOwner: AccountOwner,
    val identity: OwnerIdentity,
)
