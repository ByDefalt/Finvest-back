package com.example.finvest.account.repository

import com.example.finvest.account.entity.AccountEntity
import com.example.finvest.account.exeption.AccountNotCreatedException
import com.example.finvest.account.exeption.AccountNotDeleteException
import com.example.finvest.account.exeption.AccountNotUpdateException
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository

@Repository
class AccountJdbcRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) {

    fun findAllByUserId(userId: Long): List<AccountEntity> {
        return jdbcTemplate.query(
            AccountQueries.FIND_ALL_BY_USER_ID,
            MapSqlParameterSource("userId", userId)
        ) { rs, _ ->
            AccountEntity(
                id = rs.getLong("id"),
                name = rs.getString("name"),
                balance = rs.getBigDecimal("balance"),
                type = rs.getString("type"),
                userId = rs.getLong("user_id")
            )
        }
    }

    fun deleteByIdAndUserId(id: Long, userId: Long) {
        val affectedRows = jdbcTemplate.update(
            AccountQueries.DELETE_BY_ID_AND_USER_ID,
            MapSqlParameterSource()
                .addValue("id", id)
                .addValue("userId", userId)
        )
        if (affectedRows == 0) {
            throw AccountNotDeleteException()
        }
    }

    fun insertAccount(account: AccountEntity): AccountEntity {
        val keyHolder = GeneratedKeyHolder()

        val params = MapSqlParameterSource()
            .addValue("name", account.name)
            .addValue("balance", account.balance)
            .addValue("type", account.type)
            .addValue("userId", account.userId)

        val affectedRows = jdbcTemplate.update(
            AccountQueries.INSERT_ACCOUNT,
            params,
            keyHolder,
            arrayOf("id")
        )
        if (affectedRows == 0) {
            throw AccountNotCreatedException()
        }

        val id = keyHolder.key?.toLong()
            ?: throw IllegalStateException("Failed to retrieve generated account ID")

        return account.copy(id = id)
    }

    fun updateAccount(account: AccountEntity): AccountEntity {
        val affectedRows = jdbcTemplate.update(
            AccountQueries.UPDATE_ACCOUNT,
            MapSqlParameterSource()
                .addValue("id", account.id)
                .addValue("name", account.name)
                .addValue("balance", account.balance)
                .addValue("type", account.type)
                .addValue("userId", account.userId)
        )
        if (affectedRows == 0) {
            throw AccountNotUpdateException()
        }
        return account
    }
}