package defalt.fixtures

import defalt.models.Account
import defalt.models.AccountOwner
import defalt.models.AccountWithBank
import defalt.models.Bank
import defalt.models.CompteCourant
import defalt.models.NewAccountOwner
import defalt.models.Pea
import defalt.valueobject.AccountId
import defalt.valueobject.AccountOwnerId
import defalt.valueobject.AccountStatusCode
import defalt.valueobject.AccountTypeCode
import defalt.valueobject.BankBic
import defalt.valueobject.CurrencyCode
import defalt.valueobject.Email
import defalt.valueobject.ExternalUserId
import defalt.valueobject.OwnerRef
import defalt.valueobject.UserId
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

/** Test data builders for the account module. Every parameter has a sensible default. */
object AccountFixtures {
    /** Email of the internal user Romain, the authenticated user of most tests. */
    val ROMAIN_EMAIL = Email("romain@example.com")

    /** Email of a second internal user. */
    val TEST_EMAIL = Email("test@example.com")

    /** Email of an external user. */
    val JOHN_EMAIL = Email("john.doe@example.com")

    /** Id of Romain. */
    val ROMAIN_ID = UserId(1L)

    /** Id of the second internal user. */
    val TEST_ID = UserId(2L)

    /** Id of the external user. */
    val JOHN_EXTERNAL_ID = ExternalUserId(1L)

    const val PEA = "PEA"
    const val COMPTE_COURANT = "COMPTE_COURANT"

    /**
     * Active EUR account, a PEA by default.
     *
     * @param id id of the account.
     * @param typeCode type of the account.
     * @param name name of the account.
     * @param balance balance of the account, as a decimal string.
     * @param bankBic BIC of the bank holding the account.
     * @return the account.
     */
    fun account(
        id: Long = 0L,
        typeCode: String = PEA,
        name: String = "PEA Romain",
        balance: String = "25000.00",
        bankBic: String = "FTNOFRP1",
    ) = Account(
        id = AccountId(id),
        bankBic = BankBic(bankBic),
        name = name,
        balance = BigDecimal(balance),
        currencyCode = CurrencyCode("EUR"),
        createdAt = LocalDateTime.of(2024, 1, 15, 10, 0),
        closedAt = null,
        accountStatusCode = AccountStatusCode("ACTIVE"),
        description = "Plan d'épargne en actions",
        accountTypeCode = AccountTypeCode(typeCode),
    )

    /**
     * Bank of the given BIC.
     *
     * @param bic BIC of the bank.
     * @return the bank.
     */
    fun bank(bic: String = "FTNOFRP1") = Bank(bic = BankBic(bic), name = "Fortuneo", logo = null)

    /**
     * [account] with its bank.
     *
     * @param account the account.
     * @return the account with the bank of its BIC.
     */
    fun accountWithBank(account: Account = account()) = AccountWithBank(account, bank(account.bankBic.value))

    /**
     * PEA details.
     *
     * @param accountId id of the account.
     * @param depositLimit deposit limit, as a decimal string.
     * @return the PEA details.
     */
    fun pea(
        accountId: Long = 0L,
        depositLimit: String = "150000.00",
    ) = Pea(
        accountId = AccountId(accountId),
        openingDate = LocalDate.of(2022, 3, 15),
        depositLimit = BigDecimal(depositLimit),
    )

    /**
     * Current account details.
     *
     * @param accountId id of the account.
     * @return the current account details.
     */
    fun compteCourant(accountId: Long = 0L) =
        CompteCourant(
            accountId = AccountId(accountId),
            iban = "FR7630006000011234567890189",
            bic = "CMBRFR2B",
            accountNumber = "12345678901",
            overdraftLimit = BigDecimal("500.00"),
            holderName = "Romain Rousval",
        )

    /**
     * Owner to create, Romain with 100 % by default.
     *
     * @param email email of the owner.
     * @param percentage owned share, as a decimal string.
     * @return the owner to create.
     */
    fun newOwner(
        email: Email = ROMAIN_EMAIL,
        percentage: String = "100",
    ) = NewAccountOwner(email = email, ownershipPercentage = BigDecimal(percentage))

    /**
     * Persisted owner of [accountId], Romain with 100 % by default.
     *
     * @param accountId id of the owned account.
     * @param owner user owning the share.
     * @param percentage owned share, as a decimal string.
     * @param id id of the ownership.
     * @return the persisted owner.
     */
    fun owner(
        accountId: Long,
        owner: OwnerRef = OwnerRef.Internal(ROMAIN_ID),
        percentage: String = "100",
        id: Long = 0L,
    ) = AccountOwner(
        id = AccountOwnerId(id),
        accountId = AccountId(accountId),
        owner = owner,
        ownershipPercentage = BigDecimal(percentage),
    )
}
