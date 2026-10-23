package defalt.models

import defalt.valueobject.AccountId

/**
 * Data specific to the type of an account ([CompteCourant], [Livret], [Pea]...),
 * stored in a table of its own and linked to the account by [accountId].
 */
interface AccountDetails {
    /** Id of the account these details belong to. */
    val accountId: AccountId
}
