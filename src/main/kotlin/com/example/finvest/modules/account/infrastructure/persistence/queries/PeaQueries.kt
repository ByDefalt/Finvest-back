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

    const val CREATE_PEA = """
        INSERT INTO peas (account_id, opening_date, deposit_limit)
        VALUES (:accountId, :openingDate, :depositLimit);
    """

    const val UPDATE_PEA_BY_ACCOUNT_ID = """
        UPDATE peas
        SET opening_date = :openingDate,
            deposit_limit = :depositLimit
        WHERE account_id = :accountId;
    """

    const val DELETE_PEA_BY_ACCOUNT_ID = """
        DELETE FROM peas
        WHERE account_id = :accountId;
    """
}
