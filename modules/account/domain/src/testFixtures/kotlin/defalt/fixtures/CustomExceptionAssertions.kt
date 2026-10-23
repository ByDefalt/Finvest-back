package defalt.fixtures

import defalt.exception.CustomException
import defalt.exception.ErrorCode
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * Runs [block], asserts it throws a [CustomException] with the given [code] and returns it.
 *
 * @param code expected code of the exception.
 * @param block code that must fail.
 * @return the exception, to check its params.
 */
inline fun assertFailsWithCode(
    code: ErrorCode,
    block: () -> Unit,
): CustomException {
    val exception = assertFailsWith<CustomException> { block() }
    assertEquals(code, exception.code)
    return exception
}
