package defalt.logger

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ConsoleLoggerTest {
    private val out = ByteArrayOutputStream()
    private val err = ByteArrayOutputStream()
    private val originalOut = System.out
    private val originalErr = System.err

    @BeforeEach
    fun captureStreams() {
        System.setOut(PrintStream(out, true))
        System.setErr(PrintStream(err, true))
    }

    @AfterEach
    fun restoreStreams() {
        System.setOut(originalOut)
        System.setErr(originalErr)
    }

    @Test
    fun `writes trace, debug and info to stdout with level and logger name`() {
        val logger = ConsoleLogger("MyService", LogLevel.TRACE)

        logger.trace("t")
        logger.debug("d")
        logger.info("i")

        val lines = out.toString().lines().filter { it.isNotBlank() }
        assertEquals(3, lines.size)
        assertContains(lines[0], "TRACE [MyService] - t")
        assertContains(lines[1], "DEBUG [MyService] - d")
        assertContains(lines[2], "INFO [MyService] - i")
        assertTrue(lines[0].matches(Regex("""\[\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}] .*""")))
        assertEquals("", err.toString())
    }

    @Test
    fun `writes warn and error to stderr`() {
        val logger = ConsoleLogger("MyService", LogLevel.TRACE)

        logger.warn("w")
        logger.error("e")

        assertContains(err.toString(), "WARN [MyService] - w")
        assertContains(err.toString(), "ERROR [MyService] - e")
        assertEquals("", out.toString())
    }

    @Test
    fun `ignores messages below the configured level`() {
        val logger = ConsoleLogger("MyService", LogLevel.WARN)

        logger.trace("t")
        logger.debug("d")
        logger.info("i")
        logger.warn("w")

        assertEquals("", out.toString())
        assertContains(err.toString(), "WARN [MyService] - w")
    }

    @Test
    fun `prints the stack trace of an error`() {
        ConsoleLogger("MyService", LogLevel.ERROR).error("failed", IllegalStateException("boom"))

        assertContains(err.toString(), "ERROR [MyService] - failed")
        assertContains(err.toString(), "java.lang.IllegalStateException: boom")
    }

    @Test
    fun `isLevelEnabled compares the level priorities`() {
        val logger = ConsoleLogger("MyService", LogLevel.INFO)

        assertFalse(logger.isLevelEnabled(LogLevel.DEBUG))
        assertTrue(logger.isLevelEnabled(LogLevel.INFO))
        assertTrue(logger.isLevelEnabled(LogLevel.ERROR))
    }
}
