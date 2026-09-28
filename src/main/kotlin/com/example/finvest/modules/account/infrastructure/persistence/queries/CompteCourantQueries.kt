package com.example.finvest.modules.account.infrastructure.persistence.queries

object CompteCourantQueries {
    const val GET_COMPTE_COURANT_BY_USER_ID = """
        SELECT
            cc.account_id AS accountId,
            cc.iban AS iban,
            cc.bic AS bic,
            cc.account_number AS accountNumber,
            cc.overdraft_limit AS overdraftLimit,
            cc.holder_name AS holderName
        FROM compte_courants cc
        JOIN accounts a ON cc.account_id = a.id
        JOIN account_owners ao ON a.id = ao.account_id
        WHERE ao.user_id = :userId
    """

    const val CREATE_COMPTE_COURANT = """
        INSERT INTO compte_courants (account_id, iban, bic, account_number, overdraft_limit, holder_name)
        VALUES (:accountId, :iban, :bic, :accountNumber, :overdraftLimit, :holderName)
    """

    const val UPDATE_COMPTE_COURANT = """
        UPDATE compte_courants
        SET iban = :iban,
            bic = :bic,
            account_number = :accountNumber,
            overdraft_limit = :overdraftLimit,
            holder_name = :holderName
        WHERE account_id = :accountId
    """

    const val DELETE_COMPTE_COURANT = """
        DELETE FROM compte_courants
        WHERE account_id = :accountId
    """
}
