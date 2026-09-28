package defalt.exception

class CustomException(
    val code: ErrorCode,
    message: String,
    cause: Throwable? = null,
) : RuntimeException(message, cause) {
    private val _params = mutableMapOf<String, Any>()
    val params: Map<String, Any> get() = _params

    var locked = false
        private set

    fun setProperty(key: String, value: Any) = apply {
        check(!locked) { "Cannot modify properties after lock" }
        _params[key] = value
    }

    fun lock() = apply { locked = true }
}