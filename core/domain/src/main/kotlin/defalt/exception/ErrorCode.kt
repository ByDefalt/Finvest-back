package defalt.exception

/** Code of an error, implemented by the error code enums of each module (`CommonErrorCode`, `AuthErrorCode`...). */
interface ErrorCode {
    /** Code sent to the front-end in the `code` property of the error response, used as translation key. */
    val name: String

    /** HTTP status category of the error response. */
    val http: ErrorHttp
}
