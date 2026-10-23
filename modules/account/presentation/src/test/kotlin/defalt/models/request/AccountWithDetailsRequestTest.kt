package defalt.models.request

import defalt.mapper.ALL_DETAILS_MAPPERS
import defalt.mapper.AccountDetailsJacksonModule
import defalt.mapper.AccountDetailsMapperResolver
import defalt.mapper.DETAILS_SAMPLES
import defalt.mapper.toCommand
import defalt.models.dto.CompteTitresDetailsDto
import jakarta.validation.Validation
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import tools.jackson.core.JacksonException
import tools.jackson.databind.exc.InvalidTypeIdException
import tools.jackson.databind.json.JsonMapper
import tools.jackson.module.kotlin.KotlinModule
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertTrue

/** JSON contract of the create / update body: polymorphic details and bean validation. */
class AccountWithDetailsRequestTest {
    private val jsonMapper =
        JsonMapper
            .builder()
            .addModule(KotlinModule.Builder().build())
            .addModule(AccountDetailsJacksonModule(ALL_DETAILS_MAPPERS))
            .build()
    private val validator = Validation.buildDefaultValidatorFactory().validator
    private val detailsResolver = AccountDetailsMapperResolver(ALL_DETAILS_MAPPERS)

    @TestFactory
    fun `deserializes the details into the DTO of the account type`() =
        DETAILS_SAMPLES.map { sample ->
            dynamicTest(sample.accountTypeCode) {
                val request = read(body(sample.accountTypeCode, sample.json))

                assertEquals(sample.accountTypeCode, request.accountTypeCode)
                assertEquals(sample.dtoType, request.details.javaClass)
                assertEquals(sample.domain, request.toCommand(detailsResolver).details)
            }
        }

    @Test
    fun `accepts the type after the details`() {
        val request =
            read(
                """{"details":{"accountId":0,"accountNumber":"CT-1"},"accountTypeCode":"COMPTE_TITRES",
                "account":$ACCOUNT,"owners":$OWNERS}""",
            )

        assertIs<CompteTitresDetailsDto>(request.details)
    }

    @Test
    fun `rejects an unknown account type`() {
        assertFailsWith<InvalidTypeIdException> { read(body("UNKNOWN", "{}")) }
    }

    @Test
    fun `rejects a body without account type`() {
        assertFailsWith<JacksonException> {
            read("""{"account":$ACCOUNT,"details":{"accountId":0,"accountNumber":"CT-1"},"owners":$OWNERS}""")
        }
    }

    @Test
    fun `rejects details missing a required field`() {
        assertFailsWith<JacksonException> {
            read(body("PEA", """{"accountId":0,"openingDate":"2021-05-10"}"""))
        }
    }

    @Test
    fun `a valid request has no violation`() {
        DETAILS_SAMPLES.forEach { sample ->
            assertTrue(violations(body(sample.accountTypeCode, sample.json)).isEmpty(), sample.accountTypeCode)
        }
    }

    @Test
    fun `validates the fields of the concrete details`() {
        val violations =
            violations(
                body(
                    "COMPTE_COURANT",
                    """{"accountId":0,"iban":" ","bic":"B","accountNumber":"1","overdraftLimit":-1,"holderName":"h"}""",
                ),
            )

        assertEquals(setOf("details.iban" to "NotBlank", "details.overdraftLimit" to "PositiveOrZero"), violations)
    }

    @Test
    fun `validates dates and amounts of the details`() {
        val violations = violations(body("PEA", """{"accountId":0,"openingDate":"2999-01-01","depositLimit":0}"""))

        assertEquals(setOf("details.openingDate" to "PastOrPresent", "details.depositLimit" to "Positive"), violations)
    }

    @Test
    fun `validates the account and the owners`() {
        val account = ACCOUNT.replace("\"name\":\"PEA\"", "\"name\":\"\"")
        val violations =
            violations(
                """{"accountTypeCode":"COMPTE_TITRES","account":$account,
                "details":{"accountId":0,"accountNumber":"CT-1"},
                "owners":[{"ownershipPercentage":100,"email":"not-an-email"}]}""",
            )

        assertEquals(setOf("account.name" to "NotBlank", "owners[0].email" to "Email"), violations)
    }

    @Test
    fun `requires at least one owner`() {
        val violations =
            violations(
                """{"accountTypeCode":"COMPTE_TITRES","account":$ACCOUNT,"details":{"accountId":0,"accountNumber":"CT-1"},"owners":[]}""",
            )

        assertEquals(setOf("owners" to "NotEmpty"), violations)
    }

    private fun read(json: String): AccountWithDetailsRequest = jsonMapper.readValue(json, AccountWithDetailsRequest::class.java)

    private fun violations(json: String): Set<Pair<String, String?>> =
        validator
            .validate(read(json))
            .map { it.propertyPath.toString() to it.constraintDescriptor.annotation.annotationClass.simpleName }
            .toSet()

    private fun body(
        accountTypeCode: String,
        details: String,
    ) = """{"accountTypeCode":"$accountTypeCode","account":$ACCOUNT,"details":$details,"owners":$OWNERS}"""

    private companion object {
        const val ACCOUNT =
            """{"id":0,"bankBic":"FTNOFRP1","name":"PEA","balance":100,"currencyCode":"EUR",
                "createdAt":"2024-01-15T10:00:00","closedAt":null,"accountStatusCode":"ACTIVE","description":null}"""
        const val OWNERS = """[{"ownershipPercentage":100,"email":"romain@example.com"}]"""
    }
}
