package com.example.finvest.modules.account.infrastructure.persistence.queries

object CompteCourantQueries {

    const val GET_COMPTES_COURANTS_BY_USER_ID = """
        SELECT
            cc.account_id AS accountId,
            cc.iban AS iban,
            cc.bic AS bic,
            cc.account_number AS accountNumber,
            cc.overdraft_limit AS overdraftLimit,
            cc.holder_name AS holderName
        FROM compte_courant cc
        JOIN account a ON cc.account_id = a.id
        JOIN account_owner ao ON a.id = ao.account_id
        WHERE ao.user_id = :userId
    """
}