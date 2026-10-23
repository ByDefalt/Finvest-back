package defalt.exception

/**
 * The only exception raised by the application. Its [code] is sent to the front-end, which translates it,
 * and its [params] give the values the translated message needs.
 *
 * ```
 * throw CustomException(AccountErrorCode.OWNER_NOT_FOUND, "Owner not found")
 *     .setProperty("emails", listOf(email))
 *     .lock()
 * ```
 *
 * @property code translatable error code, which also gives the HTTP status of the response.
 * @param message technical message for the logs, never sent to the client.
 * @param cause underlying exception, if any.
 */
class CustomException(
    val code: ErrorCode,
    message: String,
    cause: Throwable? = null,
) : RuntimeException(message, cause) {
    private val _params = mutableMapOf<String, Any>()

    /** Values attached to the error and added to the response body. */
    val params: Map<String, Any> get() = _params

    /** Whether [params] can no longer be modified, see [lock]. */
    var locked = false
        private set

    /**
     * Adds a value to [params].
     *
     * @param key name of the property in the error response.
     * @param value value of the property.
     * @return this exception, to chain the calls.
     * @throws IllegalStateException if the exception is [locked] (programming error).
     */
    fun setProperty(
        key: String,
        value: Any,
    ) = apply {
        check(!locked) { "Cannot modify properties after lock" }
        _params[key] = value
    }

    /**
     * Freezes [params]: no property can be added anymore.
     *
     * @return this exception, to chain the calls.
     */
    fun lock() = apply { locked = true }
}
