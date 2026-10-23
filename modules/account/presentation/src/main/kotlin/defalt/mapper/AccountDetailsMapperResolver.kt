package defalt.mapper

import defalt.exception.AccountErrorCode
import defalt.exception.CustomException
import defalt.models.AccountDetails
import defalt.models.dto.AccountDetailsDto
import defalt.valueobject.AccountTypeCode
import org.springframework.stereotype.Component

/**
 * Dispatches the mapping of the account details to the [AccountDetailsMapperSolver] of the account type.
 *
 * @param mappers one details mapper per supported account type.
 */
@Component
class AccountDetailsMapperResolver(
    private val mappers: List<AccountDetailsMapperSolver<*, *>>,
) {
    /**
     * API representation of the details of an account of type [accountTypeCode].
     *
     * @param accountTypeCode type of the account (`PEA`, `LIVRET`...).
     * @param details domain details of the account.
     * @return the DTO of the details.
     * @throws CustomException `INVALID_ACCOUNT_TYPE` if no mapper supports the type.
     */
    fun toDto(
        accountTypeCode: AccountTypeCode,
        details: AccountDetails,
    ): AccountDetailsDto = mapperFor(accountTypeCode).toDto(details)

    /**
     * Domain details of [details], an account of type [accountTypeCode].
     *
     * @param accountTypeCode type of the account (`PEA`, `LIVRET`...).
     * @param details DTO of the details.
     * @return the domain details.
     * @throws CustomException `INVALID_ACCOUNT_TYPE` if no mapper supports the type.
     */
    fun toDomain(
        accountTypeCode: AccountTypeCode,
        details: AccountDetailsDto,
    ): AccountDetails = mapperFor(accountTypeCode).toDomain(details)

    /**
     * Mapper of [accountTypeCode].
     *
     * @param accountTypeCode type of the account (`PEA`, `LIVRET`...).
     * @return the mapper supporting [accountTypeCode].
     * @throws CustomException `INVALID_ACCOUNT_TYPE` if no mapper supports the type.
     */
    @Suppress("UNCHECKED_CAST")
    private fun mapperFor(accountTypeCode: AccountTypeCode): AccountDetailsMapperSolver<AccountDetails, AccountDetailsDto> =
        mappers.find { it.supports(accountTypeCode) } as AccountDetailsMapperSolver<AccountDetails, AccountDetailsDto>?
            ?: throw CustomException(
                code = AccountErrorCode.INVALID_ACCOUNT_TYPE,
                message = "No mapper found for account type code: ${accountTypeCode.value}",
            )
}
