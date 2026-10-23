package defalt.command

import defalt.models.Account
import defalt.models.AccountDetails
import defalt.models.NewAccountOwner

/**
 * Request to create an account.
 *
 * @property account the account to create; its id is ignored.
 * @property details its type-specific data, whose account id is ignored.
 * @property owners its owners, known by their email.
 */
data class CreateAccountCommand(
    val account: Account,
    val details: AccountDetails,
    val owners: List<NewAccountOwner>,
)
