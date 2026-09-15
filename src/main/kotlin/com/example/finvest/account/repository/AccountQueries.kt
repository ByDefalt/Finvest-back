package com.example.finvest.account.repository

object AccountQueries {

    const val FIND_BY_ID = """
        SELECT id,
               bank_id,
               name,
               balance,
               currency_id,
               created_at,
               closed_at,
               account_status_id,
               description
        FROM accounts
        WHERE id = :id
    """

    const val FIND_ALL_BY_USER_ID = """
        SELECT a.id,
               a.bank_id,
               a.name,
               a.balance,
               a.currency_id,
               a.created_at,
               a.closed_at,
               a.account_status_id,
               a.description
        FROM accounts a
        INNER JOIN account_owners ao ON ao.account_id = a.id
        WHERE ao.user_id = :userId
    """

    const val DELETE_BY_ID_AND_USER_ID = """
        DELETE FROM accounts
        WHERE id = :id
          AND EXISTS (
              SELECT 1
              FROM account_owners ao
              WHERE ao.account_id = accounts.id
                AND ao.user_id = :userId
          )
    """

    const val INSERT_ACCOUNT = """
        INSERT INTO accounts (
            bank_id,
            name,
            balance,
            currency_id,
            account_status_id,
            description
        )
        VALUES (
            :bankId,
            :name,
            :balance,
            :currencyId,
            :accountStatusId,
            :description
        )
    """

    const val UPDATE_ACCOUNT = """
        UPDATE accounts
        SET bank_id = :bankId,
            name = :name,
            balance = :balance,
            currency_id = :currencyId,
            account_status_id = :accountStatusId,
            description = :description
        WHERE id = :id
          AND EXISTS (
              SELECT 1
              FROM account_owners ao
              WHERE ao.account_id = accounts.id
                AND ao.user_id = :userId
          )
    """

    const val FIND_BY_ID_AND_USER_ID = """
        SELECT a.id,
               a.bank_id,
               a.name,
               a.balance,
               a.currency_id,
               a.created_at,
               a.closed_at,
               a.account_status_id,
               a.description
        FROM accounts a
        INNER JOIN account_owners ao ON ao.account_id = a.id
        WHERE a.id = :id
          AND ao.user_id = :userId
    """
}