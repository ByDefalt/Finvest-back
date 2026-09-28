package com.example.finvest.modules.account.infrastructure.persistence.queries

object CompteTitreQueries {
    const val GET_COMPTE_TITRE_BY_USER_ID = """
        SELECT
            ct.account_id AS accountId,
            ct.account_number AS accountNumber
        FROM comptes_titres ct
        JOIN accounts a
            ON a.id = ct.account_id
        JOIN account_owners ao
            ON ao.account_id = a.id
        WHERE ao.user_id = :userId
    """

    const val CREATE_COMPTE_TITRE = """
        INSERT INTO comptes_titres (account_id, account_number)
        VALUES (:accountId, :accountNumber)
    """

    const val UPDATE_COMPTE_TITRE = """
        UPDATE comptes_titres
        SET account_number = :accountNumber
        WHERE account_id = :accountId
    """

    const val DELETE_COMPTE_TITRE = """
        DELETE FROM comptes_titres
        WHERE account_id = :accountId
    """
}
