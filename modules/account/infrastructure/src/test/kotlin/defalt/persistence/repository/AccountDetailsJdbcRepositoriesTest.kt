package defalt.persistence.repository

import defalt.fixtures.AccountFixtures.account
import defalt.models.AccountDetails
import defalt.models.AssuranceVie
import defalt.models.CompteCourant
import defalt.models.CompteTitres
import defalt.models.Livret
import defalt.models.Pea
import defalt.models.Pee
import defalt.models.Per
import defalt.valueobject.AccountId
import defalt.valueobject.ManagementTypeCode
import defalt.valueobject.OwnerRef
import defalt.valueobject.UserId
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import java.math.BigDecimal
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Same lifecycle for every details repository: create, read by account id / ids / user, update, delete.
 * Amounts use the scale of their SQL column so that the values read back are equal to the ones written.
 */
class AccountDetailsJdbcRepositoriesTest : AccountJdbcIntegrationTest() {
    @Autowired private lateinit var compteCourantRepository: CompteCourantJdbcRepository

    @Autowired private lateinit var livretRepository: LivretJdbcRepository

    @Autowired private lateinit var peaRepository: PeaJdbcRepository

    @Autowired private lateinit var compteTitresRepository: CompteTitresJdbcRepository

    @Autowired private lateinit var assuranceVieRepository: AssuranceVieJdbcRepository

    @Autowired private lateinit var perRepository: PerJdbcRepository

    @Autowired private lateinit var peeRepository: PeeJdbcRepository

    private val openingDate = LocalDate.of(2021, 5, 10)

    @Test
    fun `compte courant lifecycle`() =
        checkLifecycle(
            typeCode = "COMPTE_COURANT",
            table = "compte_courants",
            details = { CompteCourant(it, "FR76$it", "CMBRFR2B", "ACC-${it.value}", BigDecimal("500.0000"), "Romain") },
            update = { it.copy(overdraftLimit = BigDecimal("0.0000"), holderName = "Romain Rousval") },
            create = compteCourantRepository::createCompteCourant,
            getById = compteCourantRepository::getCompteCourantByAccountId,
            getByIds = compteCourantRepository::getComptesCourantsByAccountIds,
            getByUser = compteCourantRepository::getComptesCourantsByUserId,
            save = compteCourantRepository::updateCompteCourant,
            delete = compteCourantRepository::deleteCompteCourant,
        )

    @Test
    fun `livret lifecycle`() =
        checkLifecycle(
            typeCode = "LIVRET",
            table = "livrets",
            details = { Livret(it, BigDecimal("1.7000"), BigDecimal("22950.0000")) },
            update = { it.copy(interestRate = BigDecimal("2.4000")) },
            create = livretRepository::createLivret,
            getById = livretRepository::getLivretByAccountId,
            getByIds = livretRepository::getLivretsByAccountIds,
            getByUser = livretRepository::getLivretsByUserId,
            save = livretRepository::updateLivret,
            delete = livretRepository::deleteLivret,
        )

    @Test
    fun `pea lifecycle`() =
        checkLifecycle(
            typeCode = "PEA",
            table = "peas",
            details = { Pea(it, openingDate, BigDecimal("150000.0000")) },
            update = { it.copy(openingDate = openingDate.plusYears(1), depositLimit = BigDecimal("20000.0000")) },
            create = peaRepository::createPea,
            getById = peaRepository::getPeaByAccountId,
            getByIds = peaRepository::getPeasByAccountIds,
            getByUser = peaRepository::getPeasByUserId,
            save = peaRepository::updatePea,
            delete = peaRepository::deletePea,
        )

    @Test
    fun `compte titres lifecycle`() =
        checkLifecycle(
            typeCode = "COMPTE_TITRES",
            table = "comptes_titres",
            details = { CompteTitres(it, "CT-${it.value}") },
            update = { it.copy(accountNumber = "CT-NEW-${it.accountId.value}") },
            create = compteTitresRepository::createCompteTitres,
            getById = compteTitresRepository::getCompteTitresByAccountId,
            getByIds = compteTitresRepository::getComptesTitresByAccountIds,
            getByUser = compteTitresRepository::getComptesTitresByUserId,
            save = compteTitresRepository::updateCompteTitres,
            delete = compteTitresRepository::deleteCompteTitres,
        )

    @Test
    fun `assurance vie lifecycle`() =
        checkLifecycle(
            typeCode = "ASSURANCE_VIE",
            table = "assurances_vie",
            details = { AssuranceVie(it, "AV-${it.value}", openingDate, ManagementTypeCode("FREE_MANAGEMENT")) },
            update = { it.copy(managementTypeCode = ManagementTypeCode("MANAGED")) },
            create = assuranceVieRepository::createAssuranceVie,
            getById = assuranceVieRepository::getAssuranceVieByAccountId,
            getByIds = assuranceVieRepository::getAssuranceViesByAccountIds,
            getByUser = assuranceVieRepository::getAssuranceViesByUserId,
            save = assuranceVieRepository::updateAssuranceVie,
            delete = assuranceVieRepository::deleteAssuranceVie,
        )

    @Test
    fun `per lifecycle`() =
        checkLifecycle(
            typeCode = "PER",
            table = "pers",
            details = { Per(it, "PER-${it.value}", openingDate, ManagementTypeCode("MANAGED")) },
            update = { it.copy(contractNumber = "PER-NEW-${it.accountId.value}", openingDate = openingDate.minusDays(1)) },
            create = perRepository::createPer,
            getById = perRepository::getPerByAccountId,
            getByIds = perRepository::getPersByAccountIds,
            getByUser = perRepository::getPersByUserId,
            save = perRepository::updatePer,
            delete = perRepository::deletePer,
        )

    @Test
    fun `pee lifecycle`() =
        checkLifecycle(
            typeCode = "PEE",
            table = "pees",
            details = { Pee(it, openingDate, "Crédit Mutuel Arkéa") },
            update = { it.copy(employer = "Fortuneo") },
            create = peeRepository::createPee,
            getById = peeRepository::getPeeByAccountId,
            getByIds = peeRepository::getPeesByAccountIds,
            getByUser = peeRepository::getPeesByUserId,
            save = peeRepository::updatePee,
            delete = peeRepository::deletePee,
        )

    @Test
    fun `empty id sets return no details`() {
        assertTrue(compteCourantRepository.getComptesCourantsByAccountIds(emptySet()).isEmpty())
        assertTrue(livretRepository.getLivretsByAccountIds(emptySet()).isEmpty())
        assertTrue(peaRepository.getPeasByAccountIds(emptySet()).isEmpty())
        assertTrue(compteTitresRepository.getComptesTitresByAccountIds(emptySet()).isEmpty())
        assertTrue(assuranceVieRepository.getAssuranceViesByAccountIds(emptySet()).isEmpty())
        assertTrue(perRepository.getPersByAccountIds(emptySet()).isEmpty())
        assertTrue(peeRepository.getPeesByAccountIds(emptySet()).isEmpty())
    }

    @Test
    fun `details cannot be created for an unknown account`() {
        assertFailsWith<DataIntegrityViolationException> {
            peaRepository.createPea(Pea(AccountId(-1L), openingDate, BigDecimal("1.0000")))
        }
    }

    private fun <T : AccountDetails> checkLifecycle(
        typeCode: String,
        table: String,
        details: (AccountId) -> T,
        update: (T) -> T,
        create: (T) -> Unit,
        getById: (AccountId) -> T?,
        getByIds: (Set<AccountId>) -> List<T>,
        getByUser: (UserId) -> List<T>,
        save: (T) -> Unit,
        delete: (AccountId) -> Unit,
    ) {
        val romainAccount = persistAccount(account(typeCode = typeCode))
        val otherAccount = persistAccount(account(typeCode = typeCode), owner = OwnerRef.Internal(testUserId))
        val romainDetails = details(romainAccount)
        val otherDetails = details(otherAccount)

        create(romainDetails)
        create(otherDetails)

        assertEquals(romainDetails, getById(romainAccount), "$typeCode read by account id")
        assertEquals(setOf(romainDetails, otherDetails), getByIds(setOf(romainAccount, otherAccount)).toSet(), "$typeCode read by ids")
        assertEquals(listOf(romainDetails), getByUser(romainId), "$typeCode read by user")

        val updated = update(romainDetails)
        save(updated)
        assertEquals(updated, getById(romainAccount), "$typeCode after update")
        assertEquals(otherDetails, getById(otherAccount), "$typeCode update leaks to another account")

        delete(romainAccount)
        assertNull(getById(romainAccount), "$typeCode after delete")
        assertEquals(0, count(table, romainAccount))
        assertEquals(otherDetails, getById(otherAccount), "$typeCode delete leaks to another account")
    }
}
