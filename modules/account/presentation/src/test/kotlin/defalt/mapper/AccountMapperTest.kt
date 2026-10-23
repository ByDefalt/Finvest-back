package defalt.mapper

import defalt.fixtures.AccountFixtures.JOHN_EMAIL
import defalt.fixtures.AccountFixtures.JOHN_EXTERNAL_ID
import defalt.fixtures.AccountFixtures.ROMAIN_EMAIL
import defalt.fixtures.AccountFixtures.account
import defalt.fixtures.AccountFixtures.accountWithBank
import defalt.fixtures.AccountFixtures.newOwner
import defalt.fixtures.AccountFixtures.owner
import defalt.fixtures.AccountFixtures.pea
import defalt.models.AccountOwnerWithIdentity
import defalt.models.AccountWithDetails
import defalt.models.OwnerIdentity
import defalt.models.dto.PeaDetailsDto
import defalt.models.request.AccountOwnerRequest
import defalt.models.request.AccountRequest
import defalt.models.request.AccountWithDetailsRequest
import defalt.valueobject.AccountId
import defalt.valueobject.AccountTypeCode
import defalt.valueobject.OwnerRef
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.test.assertEquals

class AccountMapperTest {
    private val detailsResolver = AccountDetailsMapperResolver(ALL_DETAILS_MAPPERS)

    private val request =
        AccountWithDetailsRequest(
            accountTypeCode = "PEA",
            account =
                AccountRequest(
                    id = 999L,
                    bankBic = "FTNOFRP1",
                    name = "PEA Romain",
                    balance = BigDecimal("25000.00"),
                    currencyCode = "EUR",
                    createdAt = LocalDateTime.of(2024, 1, 15, 10, 0),
                    closedAt = null,
                    accountStatusCode = "ACTIVE",
                    description = "Plan d'épargne en actions",
                ),
            details = PeaDetailsDto(0L, LocalDate.of(2022, 3, 15), BigDecimal("150000.00")),
            owners = listOf(AccountOwnerRequest(BigDecimal("100"), "romain@example.com")),
        )

    @Test
    fun `toCommand maps the account, its details and its owners`() {
        val command = request.toCommand(detailsResolver)

        assertEquals(account(id = 999L), command.account)
        assertEquals(pea(), command.details)
        assertEquals(listOf(newOwner(ROMAIN_EMAIL, "100")), command.owners)
    }

    @Test
    fun `toUpdateCommand uses the account id of the path, not the one of the body`() {
        val command = request.toUpdateCommand(42L, detailsResolver)

        assertEquals(AccountId(42L), command.account.id)
        assertEquals(AccountTypeCode("PEA"), command.account.accountTypeCode)
    }

    @Test
    fun `toResponse exposes the account, its bank, details and owner identities`() {
        val account = account(id = 1L)
        val response =
            AccountWithDetails(
                account = accountWithBank(account),
                details = pea(accountId = 1L),
                owners =
                    listOf(
                        AccountOwnerWithIdentity(
                            owner(accountId = 1L, owner = OwnerRef.External(JOHN_EXTERNAL_ID), percentage = "100"),
                            OwnerIdentity("John", "Doe", JOHN_EMAIL),
                        ),
                    ),
            ).toResponse(detailsResolver)

        assertEquals(1L, response.account.id)
        assertEquals("PEA", response.account.accountTypeCode)
        assertEquals("FTNOFRP1", response.account.bank.bic)
        assertEquals(PeaDetailsDto(1L, LocalDate.of(2022, 3, 15), BigDecimal("150000.00")), response.details)
        assertEquals("john.doe@example.com", response.owners.single().email)
        assertEquals(BigDecimal("100"), response.owners.single().ownershipPercentage)
    }
}
