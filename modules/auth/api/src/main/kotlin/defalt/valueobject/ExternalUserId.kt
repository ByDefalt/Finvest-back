package defalt.valueobject

/**
 * Id of an external user (a user without credentials).
 *
 * @property value the numeric id.
 */
@JvmInline
value class ExternalUserId(
    val value: Long,
)
