package defalt.persistence.repository

import defalt.models.Per
import defalt.persistence.queries.PerQueries
import defalt.repository.PerRepository
import defalt.valueobject.AccountId
import defalt.valueobject.UserId
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

/**
 * JDBC adapter of the [PerRepository] port, on the `pers` table.
 *
 * @param jdbcTemplate runs the SQL queries.
 */
@Repository
class PerJdbcRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
) : PerRepository {
    /**
     * Details of the accounts of this type owned by the internal user.
     *
     * @param userId id of the internal user.
     * @return the PER details of the user, an empty list if there is none.
     */
    override fun getPersByUserId(userId: UserId): List<Per> =
        jdbcTemplate.query(
            PerQueries.GET_BY_USER_ID,
            MapSqlParameterSource("userId", userId.value),
        ) { rs, _ ->
            Per(
                accountId = AccountId(rs.getLong("account_id")),
                contractNumber = rs.getString("contract_number"),
                openingDate = rs.getDate("opening_date").toLocalDate(),
                managementTypeCode = defalt.valueobject.ManagementTypeCode(rs.getString("management_type_code")),
            )
        }

    /**
     * Details of the given accounts, an empty list for no id.
     *
     * @param accountIds ids of the accounts; may be empty.
     * @return the details of these accounts that exist.
     */
    override fun getPersByAccountIds(accountIds: Set<AccountId>): List<Per> =
        if (accountIds.isEmpty()) {
            emptyList()
        } else {
            jdbcTemplate.query(
                PerQueries.GET_BY_ACCOUNT_IDS,
                MapSqlParameterSource("accountIds", accountIds.map(AccountId::value)),
            ) { rs, _ ->
                Per(
                    accountId = AccountId(rs.getLong("account_id")),
                    contractNumber = rs.getString("contract_number"),
                    openingDate = rs.getDate("opening_date").toLocalDate(),
                    managementTypeCode = defalt.valueobject.ManagementTypeCode(rs.getString("management_type_code")),
                )
            }
        }

    /**
     * Details of the account, or null if it has none.
     *
     * @param accountId id of the account.
     * @return the details, or null if the account has none.
     */
    override fun getPerByAccountId(accountId: AccountId): Per? =
        jdbcTemplate
            .query(
                PerQueries.GET_BY_ID,
                MapSqlParameterSource("accountId", accountId.value),
            ) { rs, _ ->
                Per(
                    accountId = AccountId(rs.getLong("account_id")),
                    contractNumber = rs.getString("contract_number"),
                    openingDate = rs.getDate("opening_date").toLocalDate(),
                    managementTypeCode = defalt.valueobject.ManagementTypeCode(rs.getString("management_type_code")),
                )
            }.firstOrNull()

    /**
     * Persists the details of [per]'s account.
     *
     * @param per details to persist, with the id of their account.
     */
    override fun createPer(per: Per) {
        jdbcTemplate.update(PerQueries.INSERT, per.parameters())
    }

    /**
     * Updates the details of [per]'s account.
     *
     * @param per new details, with the id of their account.
     */
    override fun updatePer(per: Per) {
        jdbcTemplate.update(PerQueries.UPDATE, per.parameters())
    }

    /**
     * Deletes the details of the account.
     *
     * @param accountId id of the account whose details are deleted.
     */
    override fun deletePer(accountId: AccountId) {
        jdbcTemplate.update(
            PerQueries.DELETE,
            MapSqlParameterSource("accountId", accountId.value),
        )
    }

    /**
     * SQL parameters of these details.
     *
     * @receiver details to write.
     * @return the named parameters of the SQL queries.
     */
    private fun Per.parameters() =
        MapSqlParameterSource()
            .addValue("accountId", accountId.value)
            .addValue("contractNumber", contractNumber)
            .addValue("openingDate", openingDate)
            .addValue("managementTypeCode", managementTypeCode.value)
}
