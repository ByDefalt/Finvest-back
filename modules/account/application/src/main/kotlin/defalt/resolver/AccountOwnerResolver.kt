package defalt.resolver

import defalt.exception.AccountErrorCode
import defalt.exception.CustomException
import defalt.valueobject.Email
import defalt.valueobject.OwnerRef

/** Finds the users behind the emails of the owners of an account. */
interface AccountOwnerResolver {
    /**
     * Resolves each email to an internal user or, failing that, to an external user.
     *
     * @param emails emails of the owners.
     * @return the owner of every email.
     * @throws defalt.exception.CustomException `OWNER_NOT_FOUND` if an email matches no user, with the unknown `emails`.
     */
    fun resolve(emails: Set<Email>): Map<Email, OwnerRef>
}

/**
 * Owner resolved for [email] by [AccountOwnerResolver.resolve].
 *
 * @receiver owners resolved by [AccountOwnerResolver.resolve].
 * @param email email of the owner.
 * @return the owner of [email].
 * @throws CustomException `OWNER_NOT_FOUND` if the email was not resolved.
 */
fun Map<Email, OwnerRef>.ownerOf(email: Email): OwnerRef =
    this[email] ?: throw CustomException(
        code = AccountErrorCode.OWNER_NOT_FOUND,
        message = "Owner ${email.value} was not resolved",
    ).setProperty("emails", listOf(email.value)).lock()
