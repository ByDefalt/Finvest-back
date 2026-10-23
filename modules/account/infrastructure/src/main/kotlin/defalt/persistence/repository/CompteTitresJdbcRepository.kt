package defalt.persistence.repository

import defalt.models.CompteTitres
import defalt.persistence.queries.CompteTitresQueries
import defalt.repository.CompteTitresRepository
import defalt.valueobject.AccountId
import defalt.valueobject.UserId
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

/**
 * JDBC adapter of the [CompteTitresRepository] port, on the `comptes_titres` table.
 *
 * @param jdbcTemplate runs the SQL queries.
 */
@Repository
class CompteTitresJdbcRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
) : CompteTitresRepository {
    /**
     * Details of the accounts of this type owned by the internal user.
     *
     * @param userId id of the internal user.
     * @return the securities account details of the user, an empty list if there is none.
     */
    override fun getComptesTitresByUserId(userId: UserId): List<CompteTitres> =
        jdbcTemplate.query(
            CompteTitresQueries.GET_BY_USER_ID,
            MapSqlParameterSource("userId", userId.value),
        ) { rs, _ ->
            CompteTitres(
                accountId = AccountId(rs.getLong("account_id")),
                accountNumber = rs.getString("account_number"),
            )
        }

    /**
     * Details of the given accounts, an empty list for no id.
     *
     * @param accountIds ids of the accounts; may be empty.
     * @return the details of these accounts that exist.
     */
    override fun getComptesTitresByAccountIds(accountIds: Set<AccountId>): List<CompteTitres> =
        if (accountIds.isEmpty()) {
            emptyList()
        } else {
            jdbcTemplate.query(
                CompteTitresQueries.GET_BY_ACCOUNT_IDS,
                MapSqlParameterSource("accountIds", accountIds.map(AccountId::value)),
            ) { rs, _ ->
                CompteTitres(
                    accountId = AccountId(rs.getLong("account_id")),
                    accountNumber = rs.getString("account_number"),
                )
            }
        }

    /**
     * Details of the account, or null if it has none.
     *
     * @param accountId id of the account.
     * @return the details, or null if the account has none.
     */
    override fun getCompteTitresByAccountId(accountId: AccountId): CompteTitres? =
        jdbcTemplate
            .query(
                CompteTitresQueries.GET_BY_ID,
                MapSqlParameterSource("accountId", accountId.value),
            ) { rs, _ ->
                CompteTitres(
                    accountId = AccountId(rs.getLong("account_id")),
                    accountNumber = rs.getString("account_number"),
                )
            }.firstOrNull()

    /**
     * Persists the details of [compteTitres]'s account.
     *
     * @param compteTitres details to persist, with the id of their account.
     */
    override fun createCompteTitres(compteTitres: CompteTitres) {
        jdbcTemplate.update(CompteTitresQueries.INSERT, compteTitres.parameters())
    }

    /**
     * Updates the details of [compteTitres]'s account.
     *
     * @param compteTitres new details, with the id of their account.
     */
    override fun updateCompteTitres(compteTitres: CompteTitres) {
        jdbcTemplate.update(CompteTitresQueries.UPDATE, compteTitres.parameters())
    }

    /**
     * Deletes the details of the account.
     *
     * @param accountId id of the account whose details are deleted.
     */
    override fun deleteCompteTitres(accountId: AccountId) {
        jdbcTemplate.update(
            CompteTitresQueries.DELETE,
            MapSqlParameterSource("accountId", accountId.value),
        )
    }

    /**
     * SQL parameters of these details.
     *
     * @receiver details to write.
     * @return the named parameters of the SQL queries.
     */
    private fun CompteTitres.parameters() =
        MapSqlParameterSource()
            .addValue("accountId", accountId.value)
            .addValue("accountNumber", accountNumber)
}
