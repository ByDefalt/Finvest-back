package com.example.finvest.modules.account.infrastructure.persistence.repository

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.domain.models.CompteTitre
import com.example.finvest.modules.account.domain.repository.CompteTitreRepository
import com.example.finvest.modules.account.domain.valueobject.AccountId
import com.example.finvest.modules.account.infrastructure.persistence.mapper.toDomain
import com.example.finvest.modules.account.infrastructure.persistence.models.CompteTitreEntity
import com.example.finvest.modules.account.infrastructure.persistence.queries.CompteTitreQueries
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class CompteTitreJdbcRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val logger: Logger,
) : CompteTitreRepository {
    override fun getCompteTitreByUserId(userId: Long): List<CompteTitre> =
        jdbcTemplate.query(
            CompteTitreQueries.GET_COMPTE_TITRE_BY_USER_ID,
            MapSqlParameterSource()
                .addValue("userId", userId),
        ) { rs, _ ->
            CompteTitreEntity(
                accountId = rs.getLong("accountId"),
                accountNumber = rs.getString("accountNumber"),
            ).toDomain()
        }

    override fun createCompteTitre(compteTitre: CompteTitre): AccountId {
        logger.info("Saving compte-titre")
        jdbcTemplate.update(
            CompteTitreQueries.CREATE_COMPTE_TITRE,
            MapSqlParameterSource()
                .addValue("accountId", compteTitre.accountId.value)
                .addValue("accountNumber", compteTitre.accountNumber),
        )
        logger.info("compte-titre saved: $compteTitre")
        return compteTitre.accountId
    }

    override fun updateCompteTitre(compteTitre: CompteTitre): CompteTitre {
        logger.info("Updating compte-titre for accountId: ${compteTitre.accountId.value}")
        jdbcTemplate.update(
            CompteTitreQueries.UPDATE_COMPTE_TITRE,
            MapSqlParameterSource()
                .addValue("accountId", compteTitre.accountId.value)
                .addValue("accountNumber", compteTitre.accountNumber),
        )
        logger.info("Compte-titre updated for accountId: ${compteTitre.accountId.value}")
        return compteTitre
    }

    override fun deleteCompteTitre(accountId: AccountId) {
        logger.info("Deleting compte-titre for accountId: ${accountId.value}")
        jdbcTemplate.update(
            CompteTitreQueries.DELETE_COMPTE_TITRE,
            MapSqlParameterSource()
                .addValue("accountId", accountId.value),
        )
        logger.info("Compte-titre deleted for accountId: ${accountId.value}")
    }
}
