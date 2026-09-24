package com.example.finvest.modules.account.infrastructure.persistence.queries

object CompteTitreQueries {

    const val GET_COMPTE_TITRE_BY_USER_ID = """
        SELECT
            ct.id AS accountId,
            ct.account_number AS accountNumber,
        FROM comptes_titres ct
        JOIN accounts a
            ON a.id = ct.account_id
        JOIN account_owners ao
            ON ao.account_id = a.id
        WHERE ao.user_id = :userId;
    """
}
