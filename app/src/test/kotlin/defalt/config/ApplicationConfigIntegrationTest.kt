package defalt.config

import defalt.ApiIntegrationTest
import defalt.logger.LogLevel
import defalt.logger.NullLogger
import org.hamcrest.Matchers.endsWith
import org.hamcrest.Matchers.everyItem
import org.hamcrest.Matchers.hasItem
import org.hamcrest.Matchers.not
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.options
import kotlin.test.assertIs

class ApplicationConfigIntegrationTest : ApiIntegrationTest() {
    @Test
    fun `the OpenAPI document strips the controller suffix from tags and hides the authenticated user`() {
        mockMvc.get("/v3/api-docs").andExpect {
            status { isOk() }
            jsonPath("$.paths['/accounts'].get.tags[*]", everyItem(not(endsWith("-controller"))))
            jsonPath("$.paths['/accounts'].get.tags", hasItem("account"))
            jsonPath("$.paths['/accounts'].get.parameters[*].name", not(hasItem("authenticatedUser")))
        }
    }

    @Test
    fun `cors allows the configured front-end origin`() {
        mockMvc
            .options("/accounts") {
                header(HttpHeaders.ORIGIN, "http://localhost:5173")
                header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "PUT")
            }.andExpect {
                status { isOk() }
                header { string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:5173") }
                header { string(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true") }
            }
    }

    @Test
    fun `cors rejects another origin`() {
        mockMvc
            .options("/accounts") {
                header(HttpHeaders.ORIGIN, "http://evil.example.com")
                header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET")
            }.andExpect { status { isForbidden() } }
    }

    @Test
    fun `logging is disabled by default`() {
        val logger = LoggerConfig().loggerFactory().getLogger(ApplicationConfigIntegrationTest::class.java)

        assertIs<NullLogger>(logger)
        assert(!logger.isLevelEnabled(LogLevel.ERROR))
    }
}
