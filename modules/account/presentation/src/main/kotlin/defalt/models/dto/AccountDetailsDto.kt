package defalt.models.dto

/**
 * API representation of the details of an account. The concrete type is chosen from the
 * `accountTypeCode` of the request (see [defalt.models.request.AccountWithDetailsRequest]).
 */
interface AccountDetailsDto {
    /** Id of the account; ignored on creation and update. */
    val accountId: Long
}
