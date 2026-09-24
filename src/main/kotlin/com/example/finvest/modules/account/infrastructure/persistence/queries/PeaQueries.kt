package com.example.finvest.modules.account.infrastructure.persistence.queries

object PeaQueries {

    const val GET_PEA_BY_USER_ID = """
        SELECT
            p.account_id AS accountId,
            p.opening_date AS openingDate,
            p.deposit_limit AS depositLimit
        FROM peas p
        JOIN accounts a
            ON a.id = p.account_id
        JOIN account_owners ao
            ON ao.account_id = a.id
        WHERE ao.user_id = :userId;
    """
}
