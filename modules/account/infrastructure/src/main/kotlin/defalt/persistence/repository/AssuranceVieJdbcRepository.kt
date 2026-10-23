package defalt.persistence.repository

import defalt.models.AssuranceVie
import defalt.persistence.queries.AssuranceVieQueries
import defalt.repository.AssuranceVieRepository
import defalt.valueobject.AccountId
import defalt.valueobject.UserId
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

/**
 * JDBC adapter of the [AssuranceVieRepository] port, on the `assurances_vie` table.
 *
 * @param jdbcTemplate runs the SQL queries.
 */
@Repository
class AssuranceVieJdbcRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
) : AssuranceVieRepository {
    /**
     * Details of the accounts of this type owned by the internal user.
     *
     * @param userId id of the internal user.
     * @return the life insurance contract details of the user, an empty list if there is none.
     */
    override fun getAssuranceViesByUserId(userId: UserId): List<AssuranceVie> =
        jdbcTemplate.query(
            AssuranceVieQueries.GET_BY_USER_ID,
            MapSqlParameterSource("userId", userId.value),
        ) { rs, _ ->
            AssuranceVie(
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
    override fun getAssuranceViesByAccountIds(accountIds: Set<AccountId>): List<AssuranceVie> =
        if (accountIds.isEmpty()) {
            emptyList()
        } else {
            jdbcTemplate.query(
                AssuranceVieQueries.GET_BY_ACCOUNT_IDS,
                MapSqlParameterSource("accountIds", accountIds.map(AccountId::value)),
            ) { rs, _ ->
                AssuranceVie(
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
    override fun getAssuranceVieByAccountId(accountId: AccountId): AssuranceVie? =
        jdbcTemplate
            .query(
                AssuranceVieQueries.GET_BY_ID,
                MapSqlParameterSource("accountId", accountId.value),
            ) { rs, _ ->
                AssuranceVie(
                    accountId = AccountId(rs.getLong("account_id")),
                    contractNumber = rs.getString("contract_number"),
                    openingDate = rs.getDate("opening_date").toLocalDate(),
                    managementTypeCode = defalt.valueobject.ManagementTypeCode(rs.getString("management_type_code")),
                )
            }.firstOrNull()

    /**
     * Persists the details of [assuranceVie]'s account.
     *
     * @param assuranceVie details to persist, with the id of their account.
     */
    override fun createAssuranceVie(assuranceVie: AssuranceVie) {
        jdbcTemplate.update(AssuranceVieQueries.INSERT, assuranceVie.parameters())
    }

    /**
     * Updates the details of [assuranceVie]'s account.
     *
     * @param assuranceVie new details, with the id of their account.
     */
    override fun updateAssuranceVie(assuranceVie: AssuranceVie) {
        jdbcTemplate.update(AssuranceVieQueries.UPDATE, assuranceVie.parameters())
    }

    /**
     * Deletes the details of the account.
     *
     * @param accountId id of the account whose details are deleted.
     */
    override fun deleteAssuranceVie(accountId: AccountId) {
        jdbcTemplate.update(
            AssuranceVieQueries.DELETE,
            MapSqlParameterSource("accountId", accountId.value),
        )
    }

    /**
     * SQL parameters of these details.
     *
     * @receiver details to write.
     * @return the named parameters of the SQL queries.
     */
    private fun AssuranceVie.parameters() =
        MapSqlParameterSource()
            .addValue("accountId", accountId.value)
            .addValue("contractNumber", contractNumber)
            .addValue("openingDate", openingDate)
            .addValue("managementTypeCode", managementTypeCode.value)
}
