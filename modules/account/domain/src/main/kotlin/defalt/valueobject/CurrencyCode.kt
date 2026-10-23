package defalt.valueobject

/**
 * ISO 4217 code of a currency (`EUR`, `USD`...).
 *
 * @property value the 3-letter currency code.
 */
@JvmInline
value class CurrencyCode(
    val value: String,
)
