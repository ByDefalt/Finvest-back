package defalt.persistence.repository

import defalt.models.Pee
import defalt.persistence.queries.PeeQueries
import defalt.repository.PeeRepository
import defalt.valueobject.AccountId
import defalt.valueobject.UserId
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

/**
 * JDBC adapter of the [PeeRepository] port, on the `pees` table.
 *
 * @param jdbcTemplate runs the SQL queries.
 */
@Repository
class PeeJdbcRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
) : PeeRepository {
    /**
     * Details of the accounts of this type owned by the internal user.
     *
     * @param userId id of the internal user.
     * @return the PEE details of the user, an empty list if there is none.
     */
    override fun getPeesByUserId(userId: UserId): List<Pee> =
        jdbcTemplate.query(
            PeeQueries.GET_BY_USER_ID,
            MapSqlParameterSource("userId", userId.value),
        ) { rs, _ ->
            Pee(
                accountId = AccountId(rs.getLong("account_id")),
                openingDate = rs.getDate("opening_date").toLocalDate(),
                employer = rs.getString("employer"),
            )
        }

    /**
     * Details of the given accounts, an empty list for no id.
     *
     * @param accountIds ids of the accounts; may be empty.
     * @return the details of these accounts that exist.
     */
    override fun getPeesByAccountIds(accountIds: Set<AccountId>): List<Pee> =
        if (accountIds.isEmpty()) {
            emptyList()
        } else {
            jdbcTemplate.query(
                PeeQueries.GET_BY_ACCOUNT_IDS,
                MapSqlParameterSource("accountIds", accountIds.map(AccountId::value)),
            ) { rs, _ ->
                Pee(
                    accountId = AccountId(rs.getLong("account_id")),
                    openingDate = rs.getDate("opening_date").toLocalDate(),
                    employer = rs.getString("employer"),
                )
            }
        }

    /**
     * Details of the account, or null if it has none.
     *
     * @param accountId id of the account.
     * @return the details, or null if the account has none.
     */
    override fun getPeeByAccountId(accountId: AccountId): Pee? =
        jdbcTemplate
            .query(
                PeeQueries.GET_BY_ID,
                MapSqlParameterSource("accountId", accountId.value),
            ) { rs, _ ->
                Pee(
                    accountId = AccountId(rs.getLong("account_id")),
                    openingDate = rs.getDate("opening_date").toLocalDate(),
                    employer = rs.getString("employer"),
                )
            }.firstOrNull()

    /**
     * Persists the details of [pee]'s account.
     *
     * @param pee details to persist, with the id of their account.
     */
    override fun createPee(pee: Pee) {
        jdbcTemplate.update(PeeQueries.INSERT, pee.parameters())
    }

    /**
     * Updates the details of [pee]'s account.
     *
     * @param pee new details, with the id of their account.
     */
    override fun updatePee(pee: Pee) {
        jdbcTemplate.update(PeeQueries.UPDATE, pee.parameters())
    }

    /**
     * Deletes the details of the account.
     *
     * @param accountId id of the account whose details are deleted.
     */
    override fun deletePee(accountId: AccountId) {
        jdbcTemplate.update(
            PeeQueries.DELETE,
            MapSqlParameterSource("accountId", accountId.value),
        )
    }

    /**
     * SQL parameters of these details.
     *
     * @receiver details to write.
     * @return the named parameters of the SQL queries.
     */
    private fun Pee.parameters() =
        MapSqlParameterSource()
            .addValue("accountId", accountId.value)
            .addValue("openingDate", openingDate)
            .addValue("employer", employer)
}
