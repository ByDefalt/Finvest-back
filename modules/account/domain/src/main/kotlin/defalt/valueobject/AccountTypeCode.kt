package defalt.valueobject

/**
 * Type of an account (`COMPTE_COURANT`, `LIVRET`, `PEA`, `COMPTE_TITRES`, `ASSURANCE_VIE`, `PER`, `PEE`).
 *
 * @property value the type code.
 */
@JvmInline
value class AccountTypeCode(
    val value: String,
)
