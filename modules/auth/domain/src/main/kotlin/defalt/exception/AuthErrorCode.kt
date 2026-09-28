package defalt.exception

enum class AuthErrorCode(override val http: ErrorHttp) : ErrorCode {
    USER_NOT_FOUND(ErrorHttp.NOT_FOUND),
    INVALID_PASSWORD(ErrorHttp.BAD_REQUEST)
}