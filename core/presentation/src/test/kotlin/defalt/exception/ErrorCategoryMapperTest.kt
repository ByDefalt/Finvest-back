package defalt.exception

import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import kotlin.test.assertEquals

class ErrorCategoryMapperTest {
    @TestFactory
    fun `every error category maps to the HTTP status of the same name`() =
        ErrorHttp.entries.map { category ->
            dynamicTest(category.name) {
                assertEquals(category.name, category.toHttpStatus().name)
            }
        }
}
