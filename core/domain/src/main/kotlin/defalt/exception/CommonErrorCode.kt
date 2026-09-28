package defalt.exception

enum class CommonErrorCode(override val http: ErrorHttp) : ErrorCode {
    // Validation
    VALIDATION_FAILED(ErrorHttp.BAD_REQUEST),
    REQUIRED(ErrorHttp.BAD_REQUEST),
    INVALID(ErrorHttp.BAD_REQUEST),
    INVALID_FORMAT(ErrorHttp.BAD_REQUEST),
    TOO_SHORT(ErrorHttp.BAD_REQUEST),
    TOO_LONG(ErrorHttp.BAD_REQUEST),
    TOO_SMALL(ErrorHttp.BAD_REQUEST),
    TOO_LARGE(ErrorHttp.BAD_REQUEST),

    // Technique
    INTERNAL_ERROR(ErrorHttp.INTERNAL_SERVER_ERROR),
    NOT_IMPLEMENTED(ErrorHttp.NOT_IMPLEMENTED),
}