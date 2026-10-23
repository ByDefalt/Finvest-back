package defalt.exception

/**
 * Translatable error codes of the account module, see `ErrorCode`.
 *
 * @property http HTTP status category of the error response.
 */
enum class AccountErrorCode(
    override val http: ErrorHttp,
) : ErrorCode {
    /** The account does not exist. */
    ACCOUNT_NOT_FOUND(ErrorHttp.NOT_FOUND),

    /** The authenticated user is not an owner of the account. */
    ACCOUNT_ACCESS_DENIED(ErrorHttp.FORBIDDEN),

    /** The type of an existing account cannot be changed. */
    ACCOUNT_TYPE_IMMUTABLE(ErrorHttp.BAD_REQUEST),

    /** The account type is not supported. */
    INVALID_ACCOUNT_TYPE(ErrorHttp.BAD_REQUEST),

    /** An account needs at least one owner. */
    OWNERS_REQUIRED(ErrorHttp.BAD_REQUEST),

    /** The same email appears several times among the owners; `emails` lists them. */
    DUPLICATE_OWNER(ErrorHttp.BAD_REQUEST),

    /** The authenticated user must be one of the owners of the account. */
    CREATOR_NOT_OWNER(ErrorHttp.BAD_REQUEST),

    /** An ownership percentage is not greater than 0 and at most 100; `emails` lists the owners concerned. */
    INVALID_OWNERSHIP_PERCENTAGE(ErrorHttp.BAD_REQUEST),

    /** The ownership percentages do not add up to 100; `total` gives their sum. */
    OWNERSHIP_TOTAL_MISMATCH(ErrorHttp.BAD_REQUEST),

    /** No internal or external user has the email of an owner; `emails` lists them. */
    OWNER_NOT_FOUND(ErrorHttp.NOT_FOUND),

    // Inconsistent data in the database

    /** An account has no details in the database (inconsistent data). */
    ACCOUNT_DETAILS_NOT_FOUND(ErrorHttp.INTERNAL_SERVER_ERROR),

    /** The identity of an owner cannot be found (inconsistent data). */
    OWNER_IDENTITY_NOT_FOUND(ErrorHttp.INTERNAL_SERVER_ERROR),
}
