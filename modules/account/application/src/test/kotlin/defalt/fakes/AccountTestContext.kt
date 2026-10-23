package defalt.fakes

import defalt.fixtures.AccountFixtures.COMPTE_COURANT
import defalt.fixtures.AccountFixtures.JOHN_EMAIL
import defalt.fixtures.AccountFixtures.JOHN_EXTERNAL_ID
import defalt.fixtures.AccountFixtures.PEA
import defalt.fixtures.AccountFixtures.ROMAIN_EMAIL
import defalt.fixtures.AccountFixtures.ROMAIN_ID
import defalt.fixtures.AccountFixtures.TEST_EMAIL
import defalt.fixtures.AccountFixtures.TEST_ID
import defalt.fixtures.AccountFixtures.account
import defalt.fixtures.AccountFixtures.owner
import defalt.fixtures.AccountFixtures.pea
import defalt.logger.NullLogger
import defalt.manager.ImmediateTransactionManager
import defalt.models.Account
import defalt.models.AuthenticatedUser
import defalt.resolver.UserQueryAccountOwnerResolver
import defalt.solver.DetailsRepositoryResolver
import defalt.usecase.CreateAccountUseCase
import defalt.usecase.DeleteAccountUseCase
import defalt.usecase.GetAccountWithDetailsUseCase
import defalt.usecase.UpdateAccountUseCase
import defalt.valueobject.OwnerRef

val ROMAIN = AuthenticatedUser(ROMAIN_ID, ROMAIN_EMAIL)

/** Wires the real use cases on top of in-memory ports. */
class AccountTestContext {
    val users =
        FakeUserQueryApi()
            .withInternal(ROMAIN_ID, ROMAIN_EMAIL, "Romain", "Rousval")
            .withInternal(TEST_ID, TEST_EMAIL, "Test", "User")
            .withExternal(JOHN_EXTERNAL_ID, JOHN_EMAIL, "John", "Doe")
    val owners = InMemoryAccountOwnerRepository()
    val accounts = InMemoryAccountRepository(owners)
    val peaDetails = InMemoryDetailsRepositorySolver(PEA)
    val compteCourantDetails = InMemoryDetailsRepositorySolver(COMPTE_COURANT)
    val transactions = ImmediateTransactionManager()

    private val detailsResolver = DetailsRepositoryResolver(listOf(peaDetails, compteCourantDetails))
    private val ownerResolver = UserQueryAccountOwnerResolver(users)
    private val logger = NullLogger()

    val createAccount = CreateAccountUseCase(accounts, owners, detailsResolver, ownerResolver, transactions, logger)
    val updateAccount = UpdateAccountUseCase(accounts, owners, detailsResolver, ownerResolver, transactions, logger)
    val deleteAccount = DeleteAccountUseCase(accounts, owners, detailsResolver, transactions, logger)
    val getAccountsWithDetails = GetAccountWithDetailsUseCase(accounts, owners, users, detailsResolver, transactions, logger)

    /** Seeds a PEA account with its details, fully owned by [owner]. */
    fun seedPea(
        id: Long,
        owner: OwnerRef = OwnerRef.Internal(ROMAIN_ID),
    ): Account {
        val account = account(id = id, typeCode = PEA)
        accounts.seed(account)
        peaDetails.seed(pea(accountId = id))
        owners.seed(owner(accountId = id, owner = owner))
        return account
    }
}
