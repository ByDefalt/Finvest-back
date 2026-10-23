package defalt.policy

import defalt.exception.AccountErrorCode
import defalt.exception.CustomException
import defalt.models.Account

/** Rules of the update of an account. */
object AccountUpdatePolicy {
    /**
     * The type of an account cannot change: its details are stored in a table specific to the type.
     *
     * @param existing account as currently stored.
     * @param updated account as requested.
     * @throws defalt.exception.CustomException `ACCOUNT_TYPE_IMMUTABLE` if the type changes.
     */
    fun check(
        existing: Account,
        updated: Account,
    ) {
        if (existing.accountTypeCode != updated.accountTypeCode) {
            throw CustomException(
                code = AccountErrorCode.ACCOUNT_TYPE_IMMUTABLE,
                message =
                    "Account type cannot be changed from ${existing.accountTypeCode.value} " +
                        "to ${updated.accountTypeCode.value}",
            )
        }
    }
}
