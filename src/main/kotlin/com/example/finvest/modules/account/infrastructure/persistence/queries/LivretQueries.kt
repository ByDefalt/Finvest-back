package com.example.finvest.modules.account.infrastructure.persistence.queries

object LivretQueries {
    const val GET_LIVRET_BY_USER_ID = """
        SELECT
            l.account_id AS accountId,
            l.interest_rate AS interestRate,
            l.ceiling AS ceiling
        FROM livrets l
        JOIN accounts a
            ON a.id = l.account_id
        JOIN account_owners ao
            ON ao.account_id = a.id
        WHERE ao.user_id = :userId
    """

    const val CREATE_LIVRET = """
        INSERT INTO livrets (account_id, interest_rate, ceiling)
        VALUES (:accountId, :interestRate, :ceiling)
    """

    const val UPDATE_LIVRET_BY_ACCOUNT_ID = """
        UPDATE livrets
        SET interest_rate = :interestRate,
            ceiling = :ceiling
        WHERE account_id = :accountId
    """

    const val DELETE_LIVRET_BY_ACCOUNT_ID = """
        DELETE FROM livrets
        WHERE account_id = :accountId
    """
}
