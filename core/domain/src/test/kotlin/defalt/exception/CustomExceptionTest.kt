package defalt.exception

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

class CustomExceptionTest {
    @Test
    fun `carries its code, message and cause`() {
        val cause = IllegalStateException("root")

        val exception = CustomException(CommonErrorCode.NOT_FOUND, "missing", cause)

        assertEquals(CommonErrorCode.NOT_FOUND, exception.code)
        assertEquals("missing", exception.message)
        assertSame(cause, exception.cause)
        assertTrue(exception.params.isEmpty())
        assertFalse(exception.locked)
    }

    @Test
    fun `collects properties until it is locked`() {
        val exception =
            CustomException(CommonErrorCode.INVALID, "invalid")
                .setProperty("field", "email")
                .setProperty("max", 10)
                .lock()

        assertEquals(mapOf("field" to "email", "max" to 10), exception.params)
        assertTrue(exception.locked)
    }

    @Test
    fun `refuses new properties once locked`() {
        val exception = CustomException(CommonErrorCode.INVALID, "invalid").lock()

        assertFailsWith<IllegalStateException> { exception.setProperty("field", "email") }
        assertTrue(exception.params.isEmpty())
    }
}
