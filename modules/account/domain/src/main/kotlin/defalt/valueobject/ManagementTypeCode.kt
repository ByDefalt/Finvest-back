package defalt.valueobject

/**
 * Management of a contract (`FREE_MANAGEMENT`, `MANAGED`, `ADVISED`).
 *
 * @property value the management type code.
 */
@JvmInline
value class ManagementTypeCode(
    val value: String,
)
