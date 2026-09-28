package defalt.exception

data class FieldViolation(
    val field: String,
    val code: ErrorCode,
    val params: Map<String, Any> = emptyMap(),
)
