package com.example.finvest.modules.account.infrastructure.persistence.queries

object PeeQueries {
    const val GET_PEE_BY_USER_ID = """
        SELECT
            p.account_id AS accountId,
            p.opening_date AS openingDate,
            p.employer AS employer
        FROM pees p
        JOIN accounts a
            ON a.id = p.account_id
        JOIN account_owners ao
            ON ao.account_id = a.id
        WHERE ao.user_id = :userId;
    """

    const val CREATE_PEE = """
        INSERT INTO pees (account_id, opening_date, employer)
        VALUES (:accountId, :openingDate, :employer);
    """

    const val UPDATE_PEE_BY_ACCOUNT_ID = """
        UPDATE pees
        SET opening_date = :openingDate,
            employer = :employer
        WHERE account_id = :accountId;
    """

    const val DELETE_PEE_BY_ACCOUNT_ID = """
        DELETE FROM pees
        WHERE account_id = :accountId;
    """
}
