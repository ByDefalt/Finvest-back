package defalt.mapper

import defalt.models.AccountDetails
import defalt.models.dto.AccountDetailsDto
import defalt.valueobject.AccountTypeCode

/**
 * Maps the details of one account type between the domain and the API. Each type has its own mapper,
 * which also registers its DTO for the JSON deserialization (see [AccountDetailsJacksonModule]):
 * adding a type means adding a mapper.
 *
 * @param T domain details.
 * @param R API representation of the details.
 */
interface AccountDetailsMapperSolver<T : AccountDetails, R : AccountDetailsDto> {
    /** Account type handled by this mapper, also the JSON type id of its DTO. */
    val accountTypeCode: AccountTypeCode

    /** Class of the DTO, instantiated by Jackson for [accountTypeCode]. */
    val dtoType: Class<R>

    /**
     * Whether this mapper handles [accountTypeCode].
     *
     * @param accountTypeCode type of the account (`PEA`, `LIVRET`...).
     * @return true if this mapper handles the type.
     */
    fun supports(accountTypeCode: AccountTypeCode): Boolean = accountTypeCode == this.accountTypeCode

    /**
     * API representation of [details].
     *
     * @param details domain details.
     * @return the DTO of the details.
     */
    fun toDto(details: T): R

    /**
     * Domain details of [dto].
     *
     * @param dto DTO of the details.
     * @return the domain details.
     */
    fun toDomain(dto: R): T
}
