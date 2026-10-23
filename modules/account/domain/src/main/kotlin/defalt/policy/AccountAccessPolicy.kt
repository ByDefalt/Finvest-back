package defalt.policy

import defalt.exception.AccountErrorCode
import defalt.exception.CustomException
import defalt.models.AccountOwner
import defalt.valueobject.AccountId
import defalt.valueobject.OwnerRef
import defalt.valueobject.UserId

/** Who may manage an existing account. */
object AccountAccessPolicy {
    /**
     * Only an internal owner of the account may manage it.
     *
     * @param accountId account to manage.
     * @param owners current owners of the account.
     * @param userId authenticated user.
     * @throws defalt.exception.CustomException `ACCOUNT_ACCESS_DENIED` if the user is not an owner.
     */
    fun checkIsOwner(
        accountId: AccountId,
        owners: List<AccountOwner>,
        userId: UserId,
    ) {
        if (owners.none { it.owner == OwnerRef.Internal(userId) }) {
            throw CustomException(
                code = AccountErrorCode.ACCOUNT_ACCESS_DENIED,
                message = "User ${userId.value} is not an owner of account ${accountId.value}",
            )
        }
    }
}
