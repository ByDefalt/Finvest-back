package defalt.persistence.repository

import defalt.models.Pea
import defalt.persistence.queries.PeaQueries
import defalt.repository.PeaRepository
import defalt.valueobject.AccountId
import defalt.valueobject.UserId
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

/**
 * JDBC adapter of the [PeaRepository] port, on the `peas` table.
 *
 * @param jdbcTemplate runs the SQL queries.
 */
@Repository
class PeaJdbcRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
) : PeaRepository {
    /**
     * Details of the accounts of this type owned by the internal user.
     *
     * @param userId id of the internal user.
     * @return the PEA details of the user, an empty list if there is none.
     */
    override fun getPeasByUserId(userId: UserId): List<Pea> =
        jdbcTemplate.query(
            PeaQueries.GET_BY_USER_ID,
            MapSqlParameterSource("userId", userId.value),
        ) { rs, _ ->
            Pea(
                accountId = AccountId(rs.getLong("account_id")),
                openingDate = rs.getDate("opening_date").toLocalDate(),
                depositLimit = rs.getBigDecimal("deposit_limit"),
            )
        }

    /**
     * Details of the given accounts, an empty list for no id.
     *
     * @param accountIds ids of the accounts; may be empty.
     * @return the details of these accounts that exist.
     */
    override fun getPeasByAccountIds(accountIds: Set<AccountId>): List<Pea> =
        if (accountIds.isEmpty()) {
            emptyList()
        } else {
            jdbcTemplate.query(
                PeaQueries.GET_BY_ACCOUNT_IDS,
                MapSqlParameterSource("accountIds", accountIds.map(AccountId::value)),
            ) { rs, _ ->
                Pea(
                    accountId = AccountId(rs.getLong("account_id")),
                    openingDate = rs.getDate("opening_date").toLocalDate(),
                    depositLimit = rs.getBigDecimal("deposit_limit"),
                )
            }
        }

    /**
     * Details of the account, or null if it has none.
     *
     * @param accountId id of the account.
     * @return the details, or null if the account has none.
     */
    override fun getPeaByAccountId(accountId: AccountId): Pea? =
        jdbcTemplate
            .query(
                PeaQueries.GET_BY_ID,
                MapSqlParameterSource("accountId", accountId.value),
            ) { rs, _ ->
                Pea(
                    accountId = AccountId(rs.getLong("account_id")),
                    openingDate = rs.getDate("opening_date").toLocalDate(),
                    depositLimit = rs.getBigDecimal("deposit_limit"),
                )
            }.firstOrNull()

    /**
     * Persists the details of [pea]'s account.
     *
     * @param pea details to persist, with the id of their account.
     */
    override fun createPea(pea: Pea) {
        jdbcTemplate.update(PeaQueries.INSERT, pea.parameters())
    }

    /**
     * Updates the details of [pea]'s account.
     *
     * @param pea new details, with the id of their account.
     */
    override fun updatePea(pea: Pea) {
        jdbcTemplate.update(PeaQueries.UPDATE, pea.parameters())
    }

    /**
     * Deletes the details of the account.
     *
     * @param accountId id of the account whose details are deleted.
     */
    override fun deletePea(accountId: AccountId) {
        jdbcTemplate.update(
            PeaQueries.DELETE,
            MapSqlParameterSource("accountId", accountId.value),
        )
    }

    /**
     * SQL parameters of these details.
     *
     * @receiver details to write.
     * @return the named parameters of the SQL queries.
     */
    private fun Pea.parameters() =
        MapSqlParameterSource()
            .addValue("accountId", accountId.value)
            .addValue("openingDate", openingDate)
            .addValue("depositLimit", depositLimit)
}
