package defalt.policy

import defalt.exception.AccountErrorCode
import defalt.exception.CustomException
import defalt.models.NewAccountOwner
import defalt.valueobject.Email
import java.math.BigDecimal

/** Rules of the ownership of an account, applied when it is created or updated. */
object AccountOwnershipPolicy {
    private val FULL_OWNERSHIP = BigDecimal(100)

    /**
     * Checks the owners of an account:
     * - there is at least one owner (`OWNERS_REQUIRED`);
     * - an email appears only once (`DUPLICATE_OWNER`);
     * - the creator is one of the owners (`CREATOR_NOT_OWNER`);
     * - each percentage is greater than 0 and at most 100 (`INVALID_OWNERSHIP_PERCENTAGE`);
     * - the percentages add up to 100 (`OWNERSHIP_TOTAL_MISMATCH`).
     *
     * @param owners owners of the account.
     * @param creatorEmail email of the authenticated user.
     * @throws defalt.exception.CustomException with the code of the first broken rule, in this order.
     */
    fun check(
        owners: List<NewAccountOwner>,
        creatorEmail: Email,
    ) {
        if (owners.isEmpty()) {
            throw CustomException(
                code = AccountErrorCode.OWNERS_REQUIRED,
                message = "An account must have at least one owner",
            )
        }

        val duplicates =
            owners
                .groupingBy { it.email }
                .eachCount()
                .filterValues { it > 1 }
                .keys
        if (duplicates.isNotEmpty()) {
            throw CustomException(
                code = AccountErrorCode.DUPLICATE_OWNER,
                message = "Owners list contains duplicate emails",
            ).setProperty("emails", duplicates.map(Email::value)).lock()
        }

        if (owners.none { it.email == creatorEmail }) {
            throw CustomException(
                code = AccountErrorCode.CREATOR_NOT_OWNER,
                message = "Authenticated user must be an owner of the account",
            )
        }

        val invalidPercentages =
            owners.filter {
                it.ownershipPercentage <= BigDecimal.ZERO || it.ownershipPercentage > FULL_OWNERSHIP
            }
        if (invalidPercentages.isNotEmpty()) {
            throw CustomException(
                code = AccountErrorCode.INVALID_OWNERSHIP_PERCENTAGE,
                message = "Ownership percentages must be greater than 0 and at most 100",
            ).setProperty("emails", invalidPercentages.map { it.email.value }).lock()
        }

        val total = owners.sumOf { it.ownershipPercentage }
        if (total.compareTo(FULL_OWNERSHIP) != 0) {
            throw CustomException(
                code = AccountErrorCode.OWNERSHIP_TOTAL_MISMATCH,
                message = "Ownership percentages must add up to 100, got $total",
            ).setProperty("total", total).lock()
        }
    }
}
