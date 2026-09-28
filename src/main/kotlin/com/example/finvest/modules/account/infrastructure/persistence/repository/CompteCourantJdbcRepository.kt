package com.example.finvest.modules.account.infrastructure.persistence.repository

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.domain.models.CompteCourant
import com.example.finvest.modules.account.domain.repository.CompteCourantRepository
import com.example.finvest.modules.account.domain.valueobject.AccountId
import com.example.finvest.modules.account.infrastructure.persistence.mapper.toDomain
import com.example.finvest.modules.account.infrastructure.persistence.models.CompteCourantEntity
import com.example.finvest.modules.account.infrastructure.persistence.queries.CompteCourantQueries
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class CompteCourantJdbcRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val logger: Logger,
) : CompteCourantRepository {
    override fun getCompteCourantByUserId(userId: Long): List<CompteCourant> =
        jdbcTemplate.query(
            CompteCourantQueries.GET_COMPTE_COURANT_BY_USER_ID,
            MapSqlParameterSource()
                .addValue("userId", userId),
        ) { rs, _ ->
            CompteCourantEntity(
                accountId = rs.getLong("accountId"),
                iban = rs.getString("iban"),
                bic = rs.getString("bic"),
                accountNumber = rs.getString("accountNumber"),
                overdraftLimit = rs.getBigDecimal("overdraftLimit"),
                holderName = rs.getString("holderName"),
            ).toDomain()
        }

    override fun createCompteCourant(compteCourant: CompteCourant): AccountId {
        logger.info("Saving compte-courant")
        jdbcTemplate.update(
            CompteCourantQueries.CREATE_COMPTE_COURANT,
            MapSqlParameterSource()
                .addValue("accountId", compteCourant.accountId.value)
                .addValue("iban", compteCourant.iban)
                .addValue("bic", compteCourant.bic)
                .addValue("accountNumber", compteCourant.accountNumber)
                .addValue("overdraftLimit", compteCourant.overdraftLimit)
                .addValue("holderName", compteCourant.holderName),
        )
        logger.info("compte-courant saved: $compteCourant")
        return compteCourant.accountId
    }

    override fun updateCompteCourant(compteCourant: CompteCourant): CompteCourant {
        logger.info("Updating compte-courant")
        jdbcTemplate.update(
            CompteCourantQueries.UPDATE_COMPTE_COURANT,
            MapSqlParameterSource()
                .addValue("accountId", compteCourant.accountId.value)
                .addValue("iban", compteCourant.iban)
                .addValue("bic", compteCourant.bic)
                .addValue("accountNumber", compteCourant.accountNumber)
                .addValue("overdraftLimit", compteCourant.overdraftLimit)
                .addValue("holderName", compteCourant.holderName),
        )
        logger.info("compte-courant updated: $compteCourant")
        return compteCourant
    }

    override fun deleteCompteCourant(accountId: AccountId) {
        logger.info("Deleting compte-courant")
        jdbcTemplate.update(
            CompteCourantQueries.DELETE_COMPTE_COURANT,
            MapSqlParameterSource()
                .addValue("accountId", accountId.value),
        )
        logger.info("compte-courant deleted: $accountId")
    }
}
