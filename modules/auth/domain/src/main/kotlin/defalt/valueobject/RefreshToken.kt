package defalt.valueobject

/**
 * Long-lived JWT exchanged for access tokens, kept in an HTTP-only cookie.
 *
 * @property value the encoded JWT.
 */
@JvmInline
value class RefreshToken(
    val value: String,
)
