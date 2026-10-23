package defalt.exception

import defalt.logger.NullLogger
import jakarta.validation.Valid
import jakarta.validation.constraints.AssertTrue
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.PastOrPresent
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MockMvcResultMatchersDsl
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.validation.Errors
import org.springframework.validation.Validator
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.InitBinder
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

class GlobalExceptionHandlerTest {
    private val mockMvc: MockMvc =
        MockMvcBuilders
            .standaloneSetup(FailingController())
            .setControllerAdvice(GlobalExceptionHandler(NullLogger()))
            .build()

    // ------------------------------------------------------------------
    // Exceptions
    // ------------------------------------------------------------------

    @Test
    fun `a custom exception uses the status, the code and the params of its error code`() {
        mockMvc.get("/custom").andExpect {
            status { isNotFound() }
            jsonPath("$.code") { value("NOT_FOUND") }
            jsonPath("$.id") { value(42) }
        }
    }

    @Test
    fun `a not implemented exception answers 501`() {
        mockMvc.get("/not-implemented").andExpect {
            status { isNotImplemented() }
            jsonPath("$.code") { value("NOT_IMPLEMENTED") }
        }
    }

    @Test
    fun `an unexpected exception answers 500 without leaking its message`() {
        mockMvc.get("/boom").andExpect {
            status { isInternalServerError() }
            jsonPath("$.code") { value("INTERNAL_ERROR") }
            jsonPath("$.detail") { doesNotExist() }
        }
    }

    @Test
    fun `a custom exception of a server error answers its 5xx status`() {
        mockMvc.get("/custom-server-error").andExpect {
            status { isInternalServerError() }
            jsonPath("$.code") { value("INTERNAL_ERROR") }
        }
    }

    // ------------------------------------------------------------------
    // Spring MVC errors
    // ------------------------------------------------------------------

    @Test
    fun `an unknown route answers 404 not found`() {
        mockMvc.get("/unknown").andExpect {
            status { isNotFound() }
            jsonPath("$.code") { value("NOT_FOUND") }
        }
    }

    @Test
    fun `an unsupported method answers 405`() {
        mockMvc.get("/validate").andExpect {
            status { isMethodNotAllowed() }
            jsonPath("$.code") { value("METHOD_NOT_ALLOWED") }
        }
    }

    @Test
    fun `an unsupported content type answers 415`() {
        mockMvc
            .post("/validate") {
                contentType = MediaType.TEXT_PLAIN
                content = "hello"
            }.andExpect {
                status { isUnsupportedMediaType() }
                jsonPath("$.code") { value("UNSUPPORTED_MEDIA_TYPE") }
            }
    }

    @Test
    fun `an unacceptable response type answers 406`() {
        mockMvc.get("/json") { accept = MediaType.APPLICATION_XML }.andExpect { status { isNotAcceptable() } }
    }

    @Test
    fun `a missing cookie, header, parameter or path variable is required`() {
        mockMvc.get("/cookie").andExpect { expectRequired("session") }
        mockMvc.get("/header").andExpect { expectRequired("X-Request-Id") }
        mockMvc.get("/param").andExpect { expectRequired("page") }
        mockMvc.get("/path").andExpect { expectRequired("id") }
    }

    @Test
    fun `an unsatisfied request condition is invalid`() {
        mockMvc.get("/mode") { param("mode", "b") }.andExpect {
            status { isBadRequest() }
            jsonPath("$.code") { value("INVALID") }
        }
    }

    @Test
    fun `a value of the wrong type has an invalid format`() {
        mockMvc.get("/items/abc").andExpect {
            status { isBadRequest() }
            jsonPath("$.code") { value("INVALID_FORMAT") }
            jsonPath("$.field") { value("id") }
        }
    }

    private fun MockMvcResultMatchersDsl.expectRequired(field: String) {
        status { isBadRequest() }
        jsonPath("$.code") { value("REQUIRED") }
        jsonPath("$.field") { value(field) }
    }

    @Test
    fun `an unreadable body answers 400 invalid format`() {
        validate("{not json").andExpect {
            status { isBadRequest() }
            jsonPath("$.code") { value("INVALID_FORMAT") }
        }
    }

    // ------------------------------------------------------------------
    // Bean validation
    // ------------------------------------------------------------------

    @Test
    fun `a valid body passes`() {
        validate("{}").andExpect { status { isOk() } }
    }

    @Test
    fun `blank values are required`() {
        expectViolation("""{"required":" "}""", "required", "REQUIRED")
    }

    @Test
    fun `malformed emails have an invalid format`() {
        expectViolation("""{"email":"not-an-email"}""", "email", "INVALID_FORMAT")
    }

    @Test
    fun `too short texts report the minimum length`() {
        expectViolation("""{"name":"ab"}""", "name", "TOO_SHORT")
            .andExpect { jsonPath("$.errors[0].params.minLength") { value(3) } }
    }

    @Test
    fun `too long texts report the maximum length`() {
        expectViolation("""{"name":"abcdef"}""", "name", "TOO_LONG")
            .andExpect { jsonPath("$.errors[0].params.maxLength") { value(5) } }
    }

    @Test
    fun `too large collections report the maximum size`() {
        expectViolation("""{"tags":["a","b","c"]}""", "tags", "TOO_LONG")
            .andExpect { jsonPath("$.errors[0].params.maxLength") { value(2) } }
    }

    @Test
    fun `too small collections report the minimum size`() {
        expectViolation("""{"tags":[]}""", "tags", "TOO_SHORT")
            .andExpect { jsonPath("$.errors[0].params.minLength") { value(1) } }
    }

    @Test
    fun `too large maps report the maximum size`() {
        expectViolation("""{"labels":{"a":"1","b":"2"}}""", "labels", "TOO_LONG")
            .andExpect { jsonPath("$.errors[0].params.maxLength") { value(1) } }
    }

    @Test
    fun `too small arrays report the minimum size`() {
        expectViolation("""{"codes":[]}""", "codes", "TOO_SHORT")
            .andExpect { jsonPath("$.errors[0].params.minLength") { value(1) } }
    }

    @Test
    fun `numbers below the minimum report it`() {
        expectViolation("""{"min":0}""", "min", "TOO_SMALL")
            .andExpect { jsonPath("$.errors[0].params.min") { value(1) } }
    }

    @Test
    fun `numbers above the maximum report it`() {
        expectViolation("""{"max":11}""", "max", "TOO_LARGE")
            .andExpect { jsonPath("$.errors[0].params.max") { value(10) } }
    }

    @Test
    fun `non positive numbers are too small`() {
        expectViolation("""{"positive":0}""", "positive", "TOO_SMALL")
            .andExpect { jsonPath("$.errors[0].params.min") { value(0) } }
    }

    @Test
    fun `future dates are invalid`() {
        expectViolation("""{"date":"2999-01-01"}""", "date", "INVALID")
    }

    @Test
    fun `unmapped constraints are invalid`() {
        expectViolation("""{"accepted":false}""", "accepted", "INVALID")
            .andExpect { jsonPath("$.errors[0].params") { doesNotExist() } }
    }

    @Test
    fun `errors raised by a spring validator are invalid`() {
        mockMvc
            .post("/spring-validated") {
                contentType = MediaType.APPLICATION_JSON
                content = """{"value":"x"}"""
            }.andExpect {
                status { isBadRequest() }
                jsonPath("$.errors[0].field") { value("value") }
                jsonPath("$.errors[0].code") { value("INVALID") }
            }
    }

    @Test
    fun `reports every violation of the body`() {
        validate("""{"required":"","email":"x","min":0}""").andExpect {
            status { isBadRequest() }
            jsonPath("$.errors.length()") { value(3) }
        }
    }

    private fun validate(json: String) =
        mockMvc.post("/validate") {
            contentType = MediaType.APPLICATION_JSON
            content = json
        }

    private fun expectViolation(
        json: String,
        field: String,
        code: String,
    ) = validate(json).andExpect {
        status { isBadRequest() }
        jsonPath("$.code") { value("VALIDATION_FAILED") }
        jsonPath("$.errors.length()") { value(1) }
        jsonPath("$.errors[0].field") { value(field) }
        jsonPath("$.errors[0].code") { value(code) }
    }

    class Payload {
        @field:NotBlank
        var required: String = "ok"

        @field:Email
        var email: String = "romain@example.com"

        @field:Size(min = 3, max = 5)
        var name: String = "abcd"

        @field:Size(min = 1, max = 2)
        var tags: List<String> = listOf("a")

        @field:Size(max = 1)
        var labels: Map<String, String> = emptyMap()

        @field:Size(min = 1)
        var codes: Array<String> = arrayOf("A")

        @field:Min(1)
        var min: Int = 1

        @field:Max(10)
        var max: Int = 10

        @field:Positive
        var positive: Int = 1

        @field:PastOrPresent
        var date: LocalDate? = null

        @field:AssertTrue
        var accepted: Boolean = true
    }

    class SpringValidatedPayload {
        var value: String = ""
    }

    @RestController
    class FailingController {
        @InitBinder
        fun initBinder(binder: WebDataBinder) {
            if (binder.target is SpringValidatedPayload) {
                binder.addValidators(
                    object : Validator {
                        override fun supports(clazz: Class<*>) = clazz == SpringValidatedPayload::class.java

                        override fun validate(
                            target: Any,
                            errors: Errors,
                        ) = errors.rejectValue("value", "custom.rule")
                    },
                )
            }
        }

        @PostMapping("/validate")
        fun validate(
            @Valid @RequestBody payload: Payload,
        ) = Unit

        @PostMapping("/spring-validated")
        fun springValidated(
            @Valid @RequestBody payload: SpringValidatedPayload,
        ) = Unit

        @GetMapping("/custom")
        fun custom(): Nothing = throw CustomException(CommonErrorCode.NOT_FOUND, "missing").setProperty("id", 42).lock()

        @GetMapping("/not-implemented")
        fun notImplemented(): Nothing = TODO("later")

        @GetMapping("/boom")
        fun boom(): Nothing = error("secret internal detail")

        @GetMapping("/custom-server-error")
        fun customServerError(): Nothing = throw CustomException(CommonErrorCode.INTERNAL_ERROR, "inconsistent data")

        @GetMapping("/json", produces = [MediaType.APPLICATION_JSON_VALUE])
        fun json() = mapOf("ok" to true)

        @GetMapping("/cookie")
        fun cookie(
            @CookieValue("session") session: String,
        ) = session

        @GetMapping("/header")
        fun header(
            @RequestHeader("X-Request-Id") requestId: String,
        ) = requestId

        @GetMapping("/param")
        fun param(
            @RequestParam("page") page: Int,
        ) = page

        @GetMapping("/path")
        fun path(
            @PathVariable("id") id: Long,
        ) = id

        @GetMapping("/mode", params = ["mode=a"])
        fun mode() = Unit

        @GetMapping("/items/{id}")
        fun item(
            @PathVariable("id") id: Long,
        ) = id
    }
}
