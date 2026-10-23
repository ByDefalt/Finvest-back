package defalt.persistence.repository

import defalt.models.CompteCourant
import defalt.persistence.queries.CompteCourantQueries
import defalt.repository.CompteCourantRepository
import defalt.valueobject.AccountId
import defalt.valueobject.UserId
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

/**
 * JDBC adapter of the [CompteCourantRepository] port, on the `compte_courants` table.
 *
 * @param jdbcTemplate runs the SQL queries.
 */
@Repository
class CompteCourantJdbcRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
) : CompteCourantRepository {
    /**
     * Details of the accounts of this type owned by the internal user.
     *
     * @param userId id of the internal user.
     * @return the current account details of the user, an empty list if there is none.
     */
    override fun getComptesCourantsByUserId(userId: UserId): List<CompteCourant> =
        jdbcTemplate.query(
            CompteCourantQueries.GET_BY_USER_ID,
            MapSqlParameterSource("userId", userId.value),
        ) { rs, _ ->
            CompteCourant(
                accountId = AccountId(rs.getLong("account_id")),
                iban = rs.getString("iban"),
                bic = rs.getString("bic"),
                accountNumber = rs.getString("account_number"),
                overdraftLimit = rs.getBigDecimal("overdraft_limit"),
                holderName = rs.getString("holder_name"),
            )
        }

    /**
     * Details of the given accounts, an empty list for no id.
     *
     * @param accountIds ids of the accounts; may be empty.
     * @return the details of these accounts that exist.
     */
    override fun getComptesCourantsByAccountIds(accountIds: Set<AccountId>): List<CompteCourant> =
        if (accountIds.isEmpty()) {
            emptyList()
        } else {
            jdbcTemplate.query(
                CompteCourantQueries.GET_BY_ACCOUNT_IDS,
                MapSqlParameterSource("accountIds", accountIds.map(AccountId::value)),
            ) { rs, _ ->
                CompteCourant(
                    accountId = AccountId(rs.getLong("account_id")),
                    iban = rs.getString("iban"),
                    bic = rs.getString("bic"),
                    accountNumber = rs.getString("account_number"),
                    overdraftLimit = rs.getBigDecimal("overdraft_limit"),
                    holderName = rs.getString("holder_name"),
                )
            }
        }

    /**
     * Details of the account, or null if it has none.
     *
     * @param accountId id of the account.
     * @return the details, or null if the account has none.
     */
    override fun getCompteCourantByAccountId(accountId: AccountId): CompteCourant? =
        jdbcTemplate
            .query(
                CompteCourantQueries.GET_BY_ID,
                MapSqlParameterSource("accountId", accountId.value),
            ) { rs, _ ->
                CompteCourant(
                    accountId = AccountId(rs.getLong("account_id")),
                    iban = rs.getString("iban"),
                    bic = rs.getString("bic"),
                    accountNumber = rs.getString("account_number"),
                    overdraftLimit = rs.getBigDecimal("overdraft_limit"),
                    holderName = rs.getString("holder_name"),
                )
            }.firstOrNull()

    /**
     * Persists the details of [compteCourant]'s account.
     *
     * @param compteCourant details to persist, with the id of their account.
     */
    override fun createCompteCourant(compteCourant: CompteCourant) {
        jdbcTemplate.update(CompteCourantQueries.INSERT, compteCourant.parameters())
    }

    /**
     * Updates the details of [compteCourant]'s account.
     *
     * @param compteCourant new details, with the id of their account.
     */
    override fun updateCompteCourant(compteCourant: CompteCourant) {
        jdbcTemplate.update(CompteCourantQueries.UPDATE, compteCourant.parameters())
    }

    /**
     * Deletes the details of the account.
     *
     * @param accountId id of the account whose details are deleted.
     */
    override fun deleteCompteCourant(accountId: AccountId) {
        jdbcTemplate.update(
            CompteCourantQueries.DELETE,
            MapSqlParameterSource("accountId", accountId.value),
        )
    }

    /**
     * SQL parameters of these details.
     *
     * @receiver details to write.
     * @return the named parameters of the SQL queries.
     */
    private fun CompteCourant.parameters() =
        MapSqlParameterSource()
            .addValue("accountId", accountId.value)
            .addValue("iban", iban)
            .addValue("bic", bic)
            .addValue("accountNumber", accountNumber)
            .addValue("overdraftLimit", overdraftLimit)
            .addValue("holderName", holderName)
}
