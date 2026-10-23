package defalt.exception

/**
 * Error codes shared by every module. The code (enum name) is sent to the front-end in the `code`
 * property of every error response and is the key used to translate the message.
 *
 * @property http HTTP status category of the error response.
 */
enum class CommonErrorCode(
    override val http: ErrorHttp,
) : ErrorCode {
    // Validation

    /** The request body breaks bean validation constraints; the `errors` property lists each field in error. */
    VALIDATION_FAILED(ErrorHttp.BAD_REQUEST),

    /** A required value is missing. */
    REQUIRED(ErrorHttp.BAD_REQUEST),

    /** A value is not valid. */
    INVALID(ErrorHttp.BAD_REQUEST),

    /** A value, or the whole body, cannot be read (malformed JSON, wrong type, malformed email...). */
    INVALID_FORMAT(ErrorHttp.BAD_REQUEST),

    /** A text or a collection is too short; `minLength` gives the minimum. */
    TOO_SHORT(ErrorHttp.BAD_REQUEST),

    /** A text or a collection is too long; `maxLength` gives the maximum. */
    TOO_LONG(ErrorHttp.BAD_REQUEST),

    /** A number is too small; `min` gives the minimum. */
    TOO_SMALL(ErrorHttp.BAD_REQUEST),

    /** A number is too large; `max` gives the maximum. */
    TOO_LARGE(ErrorHttp.BAD_REQUEST),

    /** A resource could not be created. */
    CREATION_FAILED(ErrorHttp.BAD_REQUEST),

    /** A resource could not be updated. */
    UPDATE_FAILED(ErrorHttp.BAD_REQUEST),

    /** A resource could not be deleted. */
    DELETE_FAILED(ErrorHttp.BAD_REQUEST),

    /** The requested resource or route does not exist. */
    NOT_FOUND(ErrorHttp.NOT_FOUND),

    // Data

    /** A value that must be unique already exists. */
    DUPLICATE(ErrorHttp.CONFLICT),

    /** The operation breaks a data constraint (for instance a resource still referenced by other data). */
    INTEGRITY_VIOLATION(ErrorHttp.CONFLICT),

    // HTTP

    /** The HTTP method is not supported by the route. */
    METHOD_NOT_ALLOWED(ErrorHttp.METHOD_NOT_ALLOWED),

    /** The requested response format is not supported. */
    NOT_ACCEPTABLE(ErrorHttp.NOT_ACCEPTABLE),

    /** The format of the request body is not supported. */
    UNSUPPORTED_MEDIA_TYPE(ErrorHttp.UNSUPPORTED_MEDIA_TYPE),

    // Technique

    /** Unexpected server error. */
    INTERNAL_ERROR(ErrorHttp.INTERNAL_SERVER_ERROR),

    /** The feature is not implemented yet. */
    NOT_IMPLEMENTED(ErrorHttp.NOT_IMPLEMENTED),
}
