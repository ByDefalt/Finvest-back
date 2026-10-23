package defalt.auth

import defalt.ApiIntegrationTest
import jakarta.servlet.http.Cookie
import org.hamcrest.Matchers.containsInAnyOrder
import org.hamcrest.Matchers.containsString
import org.hamcrest.Matchers.not
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.ResultActionsDsl
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

/** End-to-end tests of the /auth endpoints. */
class AuthApiIntegrationTest : ApiIntegrationTest() {
    @Test
    fun `registers a user with a hashed password`() {
        register(JANE, "s3cret!").andExpect { status { isCreated() } }

        val stored =
            jdbcTemplate.queryForObject(
                "SELECT u.password FROM users u JOIN personal_informations pi ON pi.id = u.personal_information_id WHERE pi.email = '$JANE'",
                emptyMap<String, Any>(),
                String::class.java,
            )
        assertNotEquals("s3cret!", stored)
    }

    @Test
    fun `rejects an email that is already used`() {
        register(JANE, "s3cret!").andExpect { status { isCreated() } }

        register(JANE, "other").andExpect {
            status { isConflict() }
            jsonPath("$.code") { value("EMAIL_ALREADY_USED") }
        }
    }

    @Test
    fun `rejects an invalid refresh token`() {
        mockMvc.post("/auth/refresh") { cookie(Cookie("refreshToken", "not-a-jwt")) }.andExpect {
            status { isUnauthorized() }
            jsonPath("$.code") { value("INVALID_REFRESH_TOKEN") }
        }
    }

    @Test
    fun `requires the refresh token cookie`() {
        mockMvc.post("/auth/refresh").andExpect {
            status { isBadRequest() }
            jsonPath("$.code") { value("REQUIRED") }
            jsonPath("$.field") { value("refreshToken") }
        }
    }

    @Test
    fun `rejects an invalid registration`() {
        post("/auth/register", """{"email":"not-an-email","firstName":"Jane","lastName":"Smith","password":""}""").andExpect {
            status { isBadRequest() }
            jsonPath("$.code") { value("VALIDATION_FAILED") }
            jsonPath("$.errors[*].field", containsInAnyOrder("email", "password"))
        }
    }

    @Test
    fun `logs in and sets the refresh token in a secure cookie`() {
        register(JANE, "s3cret!")

        login(JANE, "s3cret!").andExpect {
            status { isOk() }
            header {
                string(HttpHeaders.SET_COOKIE, containsString("refreshToken="))
                string(HttpHeaders.SET_COOKIE, containsString("HttpOnly"))
                string(HttpHeaders.SET_COOKIE, containsString("SameSite=Strict"))
                string(HttpHeaders.SET_COOKIE, containsString("Path=/auth/refresh"))
                string(HttpHeaders.SET_COOKIE, containsString("Max-Age=2592000"))
            }
        }
    }

    @Test
    fun `rejects a wrong password`() {
        register(JANE, "s3cret!")

        login(JANE, "wrong").andExpect {
            status { isBadRequest() }
            jsonPath("$.code") { value("INVALID_PASSWORD") }
            header { doesNotExist(HttpHeaders.SET_COOKIE) }
        }
    }

    @Test
    fun `rejects an unknown user`() {
        login("ghost@example.com", "s3cret!").andExpect {
            status { isNotFound() }
            jsonPath("$.code") { value("USER_NOT_FOUND") }
        }
    }

    @Test
    fun `exchanges the refresh token for an access token that opens the protected endpoints`() {
        register(JANE, "s3cret!")
        val refreshToken = refreshTokenOf(login(JANE, "s3cret!"))

        val accessToken =
            mockMvc
                .post("/auth/refresh") { cookie(Cookie("refreshToken", refreshToken)) }
                .andExpect {
                    status { isOk() }
                    jsonPath("$.token") { isNotEmpty() }
                }.andReturn()
                .response.contentAsString
                .let { Regex(""""token"\s*:\s*"([^"]+)"""").find(it)!!.groupValues[1] }

        mockMvc
            .get("/accounts") { header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken") }
            .andExpect { status { isOk() } }
    }

    @Test
    fun `logout clears the refresh token cookie`() {
        mockMvc.post("/auth/logout").andExpect {
            status { isNoContent() }
            header {
                string(HttpHeaders.SET_COOKIE, containsString("refreshToken=;"))
                string(HttpHeaders.SET_COOKIE, containsString("Max-Age=0"))
                string(HttpHeaders.SET_COOKIE, not(containsString("Max-Age=2592000")))
            }
        }
    }

    @Test
    fun `registers an external user once`() {
        val body = """{"email":"$JANE","firstName":"Jane","lastName":"Smith"}"""

        post("/auth/register/external", body).andExpect { status { isCreated() } }
        post("/auth/register/external", body).andExpect {
            status { isConflict() }
            jsonPath("$.code") { value("EMAIL_ALREADY_USED") }
        }

        assertEquals(
            1,
            count(
                "SELECT COUNT(*) FROM users_external u JOIN personal_informations pi ON pi.id = u.personal_information_id WHERE pi.email = :email",
                mapOf("email" to JANE),
            ),
        )
    }

    private fun register(
        email: String,
        password: String,
    ) = post("/auth/register", """{"email":"$email","firstName":"Jane","lastName":"Smith","password":"$password"}""")

    private fun login(
        email: String,
        password: String,
    ) = post("/auth/login", """{"email":"$email","password":"$password"}""")

    private fun post(
        path: String,
        json: String,
    ): ResultActionsDsl =
        mockMvc.post(path) {
            contentType = MediaType.APPLICATION_JSON
            content = json
        }

    private fun refreshTokenOf(login: ResultActionsDsl): String =
        Regex("refreshToken=([^;]+)").find(login.andReturn().response.getHeader(HttpHeaders.SET_COOKIE)!!)!!.groupValues[1]

    private companion object {
        const val JANE = "jane.smith@example.com"
    }
}
