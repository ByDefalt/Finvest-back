package defalt.valueobject

/**
 * Short-lived JWT authenticating the requests, sent in the `Authorization: Bearer` header.
 *
 * @property value the encoded JWT.
 */
@JvmInline
value class AccessToken(
    val value: String,
)
