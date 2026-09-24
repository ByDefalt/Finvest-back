package com.example.finvest.modules.account.infrastructure.persistence.queries

object LivretQueries {

    const val GET_LIVRET_BY_USER_ID = """
        SELECT
            l.account_id AS accountId,
            l.interest_rate AS interestRate,
            l.ceiling AS ceiling,
        FROM livret l
        JOIN accounts a
            ON a.id = l.account_id
        JOIN account_owners ao
            ON ao.account_id = a.id
        WHERE ao.user_id = :userId;
    """
}
