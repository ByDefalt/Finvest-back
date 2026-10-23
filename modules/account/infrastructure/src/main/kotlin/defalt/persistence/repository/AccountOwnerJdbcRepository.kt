package defalt.persistence.repository

import defalt.exception.CommonErrorCode
import defalt.exception.CustomException
import defalt.logger.Logger
import defalt.models.AccountOwner
import defalt.persistence.queries.AccountOwnerQueries
import defalt.repository.AccountOwnerRepository
import defalt.valueobject.AccountId
import defalt.valueobject.AccountOwnerId
import defalt.valueobject.OwnerRef
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository

/**
 * JDBC adapter of the [AccountOwnerRepository] port, on the `account_owners` table.
 * An owner row references either an internal user (`user_id`) or an external user (`user_external_id`).
 *
 * @param jdbcTemplate runs the SQL queries.
 * @param logger logger of the class.
 */
@Repository
class AccountOwnerJdbcRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val logger: Logger,
) : AccountOwnerRepository {
    /**
     * Owners of the given accounts, an empty list for no account.
     *
     * @param accountIds ids of the accounts; may be empty.
     * @return the owners of these accounts, an empty list if there is none.
     */
    override fun getAccountOwnersByAccountIds(accountIds: Set<AccountId>): List<AccountOwner> =
        if (accountIds.isEmpty()) {
            emptyList()
        } else {
            jdbcTemplate.query(
                AccountOwnerQueries.GET_BY_ACCOUNT_IDS,
                MapSqlParameterSource("accountIds", accountIds.map(AccountId::value)),
            ) { rs, _ ->
                AccountOwner(
                    id = AccountOwnerId(rs.getLong("id")),
                    accountId = AccountId(rs.getLong("account_id")),
                    owner =
                        rs
                            .getLong("user_id")
                            .takeUnless { rs.wasNull() }
                            ?.let { OwnerRef.Internal(defalt.valueobject.UserId(it)) }
                            ?: rs
                                .getLong("user_external_id")
                                .takeUnless { rs.wasNull() }
                                ?.let { OwnerRef.External(defalt.valueobject.ExternalUserId(it)) }
                            ?: throw CustomException(CommonErrorCode.INTERNAL_ERROR, "Account owner has no owner reference"),
                    ownershipPercentage = rs.getBigDecimal("ownership_percentage"),
                )
            }
        }

    /**
     * Persists the owners, whose [defalt.valueobject.OwnerRef] must be resolved (internal or external user).
     *
     * @param owners owners to persist, with a resolved owner reference.
     * @return the generated ids, in the order of [owners].
     */
    override fun createAccountOwners(owners: List<AccountOwner>): List<AccountOwnerId> =
        owners.also { logger.debug("Creating account owners in database: count=${it.size}") }.map { owner ->
            val keyHolder = GeneratedKeyHolder()
            val parameters =
                MapSqlParameterSource()
                    .addValue("accountId", owner.accountId.value)
                    .addValue("ownershipPercentage", owner.ownershipPercentage)

            when (val ownerRef = owner.owner) {
                is OwnerRef.Internal ->
                    parameters
                        .addValue("userId", ownerRef.userId.value)
                        .addValue("userExternalId", null)

                is OwnerRef.External ->
                    parameters
                        .addValue("userId", null)
                        .addValue("userExternalId", ownerRef.externalId.value)

                is OwnerRef.Unknown ->
                    throw CustomException(
                        CommonErrorCode.INTERNAL_ERROR,
                        "Cannot create an account owner from an unresolved owner reference",
                    )
            }

            jdbcTemplate.update(
                AccountOwnerQueries.INSERT,
                parameters,
                keyHolder,
                arrayOf("id"),
            )

            keyHolder.key?.toLong()?.let(::AccountOwnerId)?.also {
                logger.debug("Account owner inserted: ownerId=${it.value}, accountId=${owner.accountId.value}")
            } ?: throw CustomException(CommonErrorCode.INTERNAL_ERROR, "Account owner creation did not return a generated id")
        }

    /**
     * Deletes every owner of the account.
     *
     * @param accountId id of the account whose owners are deleted.
     */
    override fun deleteAccountOwnersByAccountId(accountId: AccountId) {
        logger.debug("Deleting account owners in database: accountId=${accountId.value}")
        jdbcTemplate.update(
            AccountOwnerQueries.DELETE_BY_ACCOUNT_ID,
            MapSqlParameterSource("accountId", accountId.value),
        )
    }
}
