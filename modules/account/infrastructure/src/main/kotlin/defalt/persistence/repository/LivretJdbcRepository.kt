package defalt.persistence.repository

import defalt.models.Livret
import defalt.persistence.queries.LivretQueries
import defalt.repository.LivretRepository
import defalt.valueobject.AccountId
import defalt.valueobject.UserId
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

/**
 * JDBC adapter of the [LivretRepository] port, on the `livrets` table.
 *
 * @param jdbcTemplate runs the SQL queries.
 */
@Repository
class LivretJdbcRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
) : LivretRepository {
    /**
     * Details of the accounts of this type owned by the internal user.
     *
     * @param userId id of the internal user.
     * @return the regulated savings account details of the user, an empty list if there is none.
     */
    override fun getLivretsByUserId(userId: UserId): List<Livret> =
        jdbcTemplate.query(
            LivretQueries.GET_BY_USER_ID,
            MapSqlParameterSource("userId", userId.value),
        ) { rs, _ ->
            Livret(
                accountId = AccountId(rs.getLong("account_id")),
                interestRate = rs.getBigDecimal("interest_rate"),
                ceiling = rs.getBigDecimal("ceiling"),
            )
        }

    /**
     * Details of the given accounts, an empty list for no id.
     *
     * @param accountIds ids of the accounts; may be empty.
     * @return the details of these accounts that exist.
     */
    override fun getLivretsByAccountIds(accountIds: Set<AccountId>): List<Livret> =
        if (accountIds.isEmpty()) {
            emptyList()
        } else {
            jdbcTemplate.query(
                LivretQueries.GET_BY_ACCOUNT_IDS,
                MapSqlParameterSource("accountIds", accountIds.map(AccountId::value)),
            ) { rs, _ ->
                Livret(
                    accountId = AccountId(rs.getLong("account_id")),
                    interestRate = rs.getBigDecimal("interest_rate"),
                    ceiling = rs.getBigDecimal("ceiling"),
                )
            }
        }

    /**
     * Details of the account, or null if it has none.
     *
     * @param accountId id of the account.
     * @return the details, or null if the account has none.
     */
    override fun getLivretByAccountId(accountId: AccountId): Livret? =
        jdbcTemplate
            .query(
                LivretQueries.GET_BY_ID,
                MapSqlParameterSource("accountId", accountId.value),
            ) { rs, _ ->
                Livret(
                    accountId = AccountId(rs.getLong("account_id")),
                    interestRate = rs.getBigDecimal("interest_rate"),
                    ceiling = rs.getBigDecimal("ceiling"),
                )
            }.firstOrNull()

    /**
     * Persists the details of [livret]'s account.
     *
     * @param livret details to persist, with the id of their account.
     */
    override fun createLivret(livret: Livret) {
        jdbcTemplate.update(LivretQueries.INSERT, livret.parameters())
    }

    /**
     * Updates the details of [livret]'s account.
     *
     * @param livret new details, with the id of their account.
     */
    override fun updateLivret(livret: Livret) {
        jdbcTemplate.update(LivretQueries.UPDATE, livret.parameters())
    }

    /**
     * Deletes the details of the account.
     *
     * @param accountId id of the account whose details are deleted.
     */
    override fun deleteLivret(accountId: AccountId) {
        jdbcTemplate.update(
            LivretQueries.DELETE,
            MapSqlParameterSource("accountId", accountId.value),
        )
    }

    /**
     * SQL parameters of these details.
     *
     * @receiver details to write.
     * @return the named parameters of the SQL queries.
     */
    private fun Livret.parameters() =
        MapSqlParameterSource()
            .addValue("accountId", accountId.value)
            .addValue("interestRate", interestRate)
            .addValue("ceiling", ceiling)
}
