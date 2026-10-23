package defalt.exception

/**
 * Translatable error codes of the auth module, see `ErrorCode`.
 *
 * @property http HTTP status category of the error response.
 */
enum class AuthErrorCode(
    override val http: ErrorHttp,
) : ErrorCode {
    /** No user has this email or id. */
    USER_NOT_FOUND(ErrorHttp.NOT_FOUND),

    /** The password does not match the email. */
    INVALID_PASSWORD(ErrorHttp.BAD_REQUEST),

    /** The email is already used by an internal or external user. */
    EMAIL_ALREADY_USED(ErrorHttp.CONFLICT),

    /** The endpoint requires an access token in the `Authorization: Bearer` header. */
    AUTHENTICATION_REQUIRED(ErrorHttp.UNAUTHORIZED),

    /** The access token is invalid or expired: the front-end must refresh it. */
    INVALID_ACCESS_TOKEN(ErrorHttp.UNAUTHORIZED),

    /** The refresh token is invalid or expired: the user must log in again. */
    INVALID_REFRESH_TOKEN(ErrorHttp.UNAUTHORIZED),

    /** A user has no personal information (inconsistent data). */
    PERSONAL_INFORMATION_NOT_FOUND(ErrorHttp.INTERNAL_SERVER_ERROR),
}
