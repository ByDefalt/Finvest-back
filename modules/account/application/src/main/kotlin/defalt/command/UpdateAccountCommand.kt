package defalt.command

import defalt.models.Account
import defalt.models.AccountDetails
import defalt.models.NewAccountOwner

/**
 * Request to update an account: its fields, its details and the full list of its owners.
 *
 * @property account the account, with its id.
 * @property details its new type-specific data.
 * @property owners its new owners, replacing the current ones.
 */
data class UpdateAccountCommand(
    val account: Account,
    val details: AccountDetails,
    val owners: List<NewAccountOwner>,
)
