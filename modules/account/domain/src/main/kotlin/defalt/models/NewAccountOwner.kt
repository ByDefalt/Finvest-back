package defalt.models

import defalt.valueobject.AccountId
import defalt.valueobject.AccountOwnerId
import defalt.valueobject.Email
import defalt.valueobject.OwnerRef
import java.math.BigDecimal

/**
 * Owner of an account being created or updated, known by its email until it is resolved to a user.
 *
 * @property email email of the internal or external user.
 * @property ownershipPercentage owned share of the account, in percent.
 */
data class NewAccountOwner(
    val email: Email,
    val ownershipPercentage: BigDecimal,
) {
    /**
     * Ownership of [accountId] by the resolved [owner], ready to be persisted.
     *
     * @param accountId account owned.
     * @param owner user resolved from [email].
     * @return the ownership, with an id that is generated when it is persisted.
     */
    fun toAccountOwner(
        accountId: AccountId,
        owner: OwnerRef,
    ): AccountOwner =
        AccountOwner(
            id = AccountOwnerId(0L),
            accountId = accountId,
            owner = owner,
            ownershipPercentage = ownershipPercentage,
        )
}
