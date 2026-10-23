package defalt.logger

import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

class DefaultLoggerFactoryTest {
    @Test
    fun `creates console loggers named after the class when logging is enabled`() {
        val logger = DefaultLoggerFactory(enabled = true, level = LogLevel.INFO).getLogger(DefaultLoggerFactoryTest::class.java)

        assertIs<ConsoleLogger>(logger)
        assertTrue(logger.isLevelEnabled(LogLevel.INFO))
        assertFalse(logger.isLevelEnabled(LogLevel.DEBUG))
        assertContains(captureStdout { logger.info("hello") }, "[DefaultLoggerFactoryTest] - hello")
    }

    @Test
    fun `creates silent loggers when logging is disabled`() {
        val logger = DefaultLoggerFactory(enabled = false, level = LogLevel.TRACE).getLogger(DefaultLoggerFactoryTest::class.java)

        assertIs<NullLogger>(logger)
    }

    @Test
    fun `the null logger never writes and enables no level`() {
        val logger = NullLogger()

        val written = captureStdout {
            logger.trace("t")
            logger.debug("d")
            logger.info("i")
            logger.warn("w")
            logger.error("e")
            logger.error("e", IllegalStateException())
        }

        assertEquals("", written)
        LogLevel.entries.forEach { assertFalse(logger.isLevelEnabled(it)) }
    }

    private fun captureStdout(block: () -> Unit): String {
        val original = System.out
        val buffer = ByteArrayOutputStream()
        System.setOut(PrintStream(buffer, true))
        try {
            block()
        } finally {
            System.setOut(original)
        }
        return buffer.toString()
    }
}
