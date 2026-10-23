package defalt.mapper

import defalt.models.AccountDetails
import defalt.models.AssuranceVie
import defalt.models.CompteCourant
import defalt.models.CompteTitres
import defalt.models.Livret
import defalt.models.Pea
import defalt.models.Pee
import defalt.models.Per
import defalt.models.dto.AssuranceVieDetailsDto
import defalt.models.dto.CompteCourantDetailsDto
import defalt.models.dto.CompteTitresDetailsDto
import defalt.models.dto.LivretDetailsDto
import defalt.models.dto.PeaDetailsDto
import defalt.models.dto.PeeDetailsDto
import defalt.models.dto.PerDetailsDto
import defalt.valueobject.AccountId
import defalt.valueobject.ManagementTypeCode
import java.math.BigDecimal
import java.time.LocalDate

val ALL_DETAILS_MAPPERS: List<AccountDetailsMapperSolver<*, *>> =
    listOf(
        CompteCourantDetailsMapper(),
        LivretDetailsMapper(),
        PeaDetailsMapper(),
        CompteTitresDetailsMapper(),
        AssuranceVieDetailsMapper(),
        PerDetailsMapper(),
        PeeDetailsMapper(),
    )

/** One valid sample per account type: its JSON payload, expected DTO type and domain value. */
data class DetailsSample(
    val accountTypeCode: String,
    val json: String,
    val dtoType: Class<*>,
    val domain: AccountDetails,
)

private val ACCOUNT_ID = AccountId(5L)
private val OPENING_DATE = LocalDate.of(2021, 5, 10)

val DETAILS_SAMPLES =
    listOf(
        DetailsSample(
            "COMPTE_COURANT",
            """{"accountId":5,"iban":"FR76300060000112","bic":"CMBRFR2B","accountNumber":"123","overdraftLimit":500,"holderName":"Romain"}""",
            CompteCourantDetailsDto::class.java,
            CompteCourant(ACCOUNT_ID, "FR76300060000112", "CMBRFR2B", "123", BigDecimal("500"), "Romain"),
        ),
        DetailsSample(
            "LIVRET",
            """{"accountId":5,"interestRate":1.7,"ceiling":22950}""",
            LivretDetailsDto::class.java,
            Livret(ACCOUNT_ID, BigDecimal("1.7"), BigDecimal("22950")),
        ),
        DetailsSample(
            "PEA",
            """{"accountId":5,"openingDate":"2021-05-10","depositLimit":150000}""",
            PeaDetailsDto::class.java,
            Pea(ACCOUNT_ID, OPENING_DATE, BigDecimal("150000")),
        ),
        DetailsSample(
            "COMPTE_TITRES",
            """{"accountId":5,"accountNumber":"CT-1"}""",
            CompteTitresDetailsDto::class.java,
            CompteTitres(ACCOUNT_ID, "CT-1"),
        ),
        DetailsSample(
            "ASSURANCE_VIE",
            """{"accountId":5,"contractNumber":"AV-1","openingDate":"2021-05-10","managementTypeCode":"MANAGED"}""",
            AssuranceVieDetailsDto::class.java,
            AssuranceVie(ACCOUNT_ID, "AV-1", OPENING_DATE, ManagementTypeCode("MANAGED")),
        ),
        DetailsSample(
            "PER",
            """{"accountId":5,"contractNumber":"PER-1","openingDate":"2021-05-10","managementTypeCode":"FREE_MANAGEMENT"}""",
            PerDetailsDto::class.java,
            Per(ACCOUNT_ID, "PER-1", OPENING_DATE, ManagementTypeCode("FREE_MANAGEMENT")),
        ),
        DetailsSample(
            "PEE",
            """{"accountId":5,"openingDate":"2021-05-10","employer":"Arkéa"}""",
            PeeDetailsDto::class.java,
            Pee(ACCOUNT_ID, OPENING_DATE, "Arkéa"),
        ),
    )
