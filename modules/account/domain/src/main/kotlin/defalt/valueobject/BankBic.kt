package defalt.valueobject

/**
 * BIC (Bank Identifier Code) of a bank.
 *
 * @property value the BIC, 8 or 11 characters.
 */
@JvmInline
value class BankBic(
    val value: String,
)
