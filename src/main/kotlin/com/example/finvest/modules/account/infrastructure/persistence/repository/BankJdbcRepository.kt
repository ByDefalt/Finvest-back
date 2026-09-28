package com.example.finvest.modules.account.infrastructure.persistence.repository

import com.example.finvest.logger.Logger
import com.example.finvest.modules.account.domain.models.Bank
import com.example.finvest.modules.account.domain.repository.BankRepository
import com.example.finvest.modules.account.infrastructure.persistence.mapper.toDomain
import com.example.finvest.modules.account.infrastructure.persistence.models.BankEntity
import com.example.finvest.modules.account.infrastructure.persistence.queries.BankQueries
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class BankJdbcRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val logger: Logger,
) : BankRepository {
    override fun getBanks(): Map<Long, Bank> =
        jdbcTemplate
            .query(
                BankQueries.GET_BANKS,
            ) { rs, _ ->
                BankEntity(
                    id = rs.getLong("id"),
                    name = rs.getString("name"),
                    bic = rs.getString("bic"),
                    logo = rs.getString("logo"),
                ).toDomain()
            }.associateBy { it.id.value }
}
