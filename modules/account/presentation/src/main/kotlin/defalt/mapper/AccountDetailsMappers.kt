package defalt.mapper

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
import defalt.valueobject.AccountTypeCode
import defalt.valueobject.ManagementTypeCode
import org.springframework.stereotype.Component

/** Maps the current account details (`COMPTE_COURANT`). */
@Component
class CompteCourantDetailsMapper : AccountDetailsMapperSolver<CompteCourant, CompteCourantDetailsDto> {
    override val accountTypeCode = AccountTypeCode("COMPTE_COURANT")
    override val dtoType = CompteCourantDetailsDto::class.java

    /**
     * API representation of [details].
     *
     * @param details domain details.
     * @return the DTO of the details.
     */
    override fun toDto(details: CompteCourant): CompteCourantDetailsDto =
        CompteCourantDetailsDto(
            accountId = details.accountId.value,
            iban = details.iban,
            bic = details.bic,
            accountNumber = details.accountNumber,
            overdraftLimit = details.overdraftLimit,
            holderName = details.holderName,
        )

    /**
     * Domain details of [dto].
     *
     * @param dto DTO of the details.
     * @return the domain details.
     */
    override fun toDomain(dto: CompteCourantDetailsDto): CompteCourant =
        CompteCourant(
            accountId = AccountId(dto.accountId),
            iban = dto.iban,
            bic = dto.bic,
            accountNumber = dto.accountNumber,
            overdraftLimit = dto.overdraftLimit,
            holderName = dto.holderName,
        )
}

/** Maps the regulated savings account details (`LIVRET`). */
@Component
class LivretDetailsMapper : AccountDetailsMapperSolver<Livret, LivretDetailsDto> {
    override val accountTypeCode = AccountTypeCode("LIVRET")
    override val dtoType = LivretDetailsDto::class.java

    /**
     * API representation of [details].
     *
     * @param details domain details.
     * @return the DTO of the details.
     */
    override fun toDto(details: Livret): LivretDetailsDto = LivretDetailsDto(details.accountId.value, details.interestRate, details.ceiling)

    /**
     * Domain details of [dto].
     *
     * @param dto DTO of the details.
     * @return the domain details.
     */
    override fun toDomain(dto: LivretDetailsDto): Livret =
        Livret(
            accountId = AccountId(dto.accountId),
            interestRate = dto.interestRate,
            ceiling = dto.ceiling,
        )
}

/** Maps the PEA details (`PEA`). */
@Component
class PeaDetailsMapper : AccountDetailsMapperSolver<Pea, PeaDetailsDto> {
    override val accountTypeCode = AccountTypeCode("PEA")
    override val dtoType = PeaDetailsDto::class.java

    /**
     * API representation of [details].
     *
     * @param details domain details.
     * @return the DTO of the details.
     */
    override fun toDto(details: Pea): PeaDetailsDto = PeaDetailsDto(details.accountId.value, details.openingDate, details.depositLimit)

    /**
     * Domain details of [dto].
     *
     * @param dto DTO of the details.
     * @return the domain details.
     */
    override fun toDomain(dto: PeaDetailsDto): Pea =
        Pea(
            accountId = AccountId(dto.accountId),
            openingDate = dto.openingDate,
            depositLimit = dto.depositLimit,
        )
}

/** Maps the securities account details (`COMPTE_TITRES`). */
@Component
class CompteTitresDetailsMapper : AccountDetailsMapperSolver<CompteTitres, CompteTitresDetailsDto> {
    override val accountTypeCode = AccountTypeCode("COMPTE_TITRES")
    override val dtoType = CompteTitresDetailsDto::class.java

    /**
     * API representation of [details].
     *
     * @param details domain details.
     * @return the DTO of the details.
     */
    override fun toDto(details: CompteTitres): CompteTitresDetailsDto =
        CompteTitresDetailsDto(details.accountId.value, details.accountNumber)

    /**
     * Domain details of [dto].
     *
     * @param dto DTO of the details.
     * @return the domain details.
     */
    override fun toDomain(dto: CompteTitresDetailsDto): CompteTitres =
        CompteTitres(
            accountId = AccountId(dto.accountId),
            accountNumber = dto.accountNumber,
        )
}

/** Maps the life insurance contract details (`ASSURANCE_VIE`). */
@Component
class AssuranceVieDetailsMapper : AccountDetailsMapperSolver<AssuranceVie, AssuranceVieDetailsDto> {
    override val accountTypeCode = AccountTypeCode("ASSURANCE_VIE")
    override val dtoType = AssuranceVieDetailsDto::class.java

    /**
     * API representation of [details].
     *
     * @param details domain details.
     * @return the DTO of the details.
     */
    override fun toDto(details: AssuranceVie): AssuranceVieDetailsDto =
        AssuranceVieDetailsDto(
            details.accountId.value,
            details.contractNumber,
            details.openingDate,
            details.managementTypeCode.value,
        )

    /**
     * Domain details of [dto].
     *
     * @param dto DTO of the details.
     * @return the domain details.
     */
    override fun toDomain(dto: AssuranceVieDetailsDto): AssuranceVie =
        AssuranceVie(
            accountId = AccountId(dto.accountId),
            contractNumber = dto.contractNumber,
            openingDate = dto.openingDate,
            managementTypeCode = ManagementTypeCode(dto.managementTypeCode),
        )
}

/** Maps the PER details (`PER`). */
@Component
class PerDetailsMapper : AccountDetailsMapperSolver<Per, PerDetailsDto> {
    override val accountTypeCode = AccountTypeCode("PER")
    override val dtoType = PerDetailsDto::class.java

    /**
     * API representation of [details].
     *
     * @param details domain details.
     * @return the DTO of the details.
     */
    override fun toDto(details: Per): PerDetailsDto =
        PerDetailsDto(
            details.accountId.value,
            details.contractNumber,
            details.openingDate,
            details.managementTypeCode.value,
        )

    /**
     * Domain details of [dto].
     *
     * @param dto DTO of the details.
     * @return the domain details.
     */
    override fun toDomain(dto: PerDetailsDto): Per =
        Per(
            accountId = AccountId(dto.accountId),
            contractNumber = dto.contractNumber,
            openingDate = dto.openingDate,
            managementTypeCode = ManagementTypeCode(dto.managementTypeCode),
        )
}

/** Maps the PEE details (`PEE`). */
@Component
class PeeDetailsMapper : AccountDetailsMapperSolver<Pee, PeeDetailsDto> {
    override val accountTypeCode = AccountTypeCode("PEE")
    override val dtoType = PeeDetailsDto::class.java

    /**
     * API representation of [details].
     *
     * @param details domain details.
     * @return the DTO of the details.
     */
    override fun toDto(details: Pee): PeeDetailsDto = PeeDetailsDto(details.accountId.value, details.openingDate, details.employer)

    /**
     * Domain details of [dto].
     *
     * @param dto DTO of the details.
     * @return the domain details.
     */
    override fun toDomain(dto: PeeDetailsDto): Pee =
        Pee(
            accountId = AccountId(dto.accountId),
            openingDate = dto.openingDate,
            employer = dto.employer,
        )
}
