package defalt.mapper

import defalt.exception.AccountErrorCode
import defalt.fixtures.AccountFixtures.pea
import defalt.fixtures.assertFailsWithCode
import defalt.models.dto.PeaDetailsDto
import defalt.valueobject.AccountTypeCode
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import java.math.BigDecimal
import java.time.LocalDate
import kotlin.test.assertEquals

class AccountDetailsMapperResolverTest {
    private val resolver = AccountDetailsMapperResolver(ALL_DETAILS_MAPPERS)

    @TestFactory
    fun `maps the details of every account type to its DTO and back`() =
        DETAILS_SAMPLES.map { sample ->
            dynamicTest(sample.accountTypeCode) {
                val typeCode = AccountTypeCode(sample.accountTypeCode)

                val dto = resolver.toDto(typeCode, sample.domain)

                assertEquals(sample.dtoType, dto.javaClass)
                assertEquals(sample.domain, resolver.toDomain(typeCode, dto))
            }
        }

    @Test
    fun `exposes the domain values in the DTO`() {
        val dto = resolver.toDto(AccountTypeCode("PEA"), pea(accountId = 3L))

        assertEquals(PeaDetailsDto(3L, LocalDate.of(2022, 3, 15), BigDecimal("150000.00")), dto)
    }

    @Test
    fun `every account type has exactly one mapper`() {
        assertEquals(
            DETAILS_SAMPLES.map { it.accountTypeCode }.toSet(),
            ALL_DETAILS_MAPPERS.map { it.accountTypeCode.value }.toSet(),
        )
        assertEquals(ALL_DETAILS_MAPPERS.size, ALL_DETAILS_MAPPERS.map { it.accountTypeCode }.toSet().size)
    }

    @Test
    fun `fails for an account type without mapper`() {
        assertFailsWithCode(AccountErrorCode.INVALID_ACCOUNT_TYPE) {
            resolver.toDto(AccountTypeCode("UNKNOWN"), pea())
        }
    }
}
