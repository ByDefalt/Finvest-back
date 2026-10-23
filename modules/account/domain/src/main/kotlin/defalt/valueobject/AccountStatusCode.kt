package defalt.valueobject

/**
 * Status of an account (`ACTIVE`, `CLOSED`, `BLOCKED`).
 *
 * @property value the status code.
 */
@JvmInline
value class AccountStatusCode(
    val value: String,
)
