package defalt.models

import defalt.valueobject.AccountId
import defalt.valueobject.AccountOwnerId
import defalt.valueobject.OwnerRef
import java.math.BigDecimal

/**
 * Share of an account owned by a user.
 *
 * @property id id of the ownership, ignored when it is created.
 * @property accountId account owned.
 * @property owner internal or external user owning the share.
 * @property ownershipPercentage owned share of the account, in percent.
 */
data class AccountOwner(
    val id: AccountOwnerId,
    val accountId: AccountId,
    val owner: OwnerRef,
    val ownershipPercentage: BigDecimal,
)
