package com.example.finvest.account.repository

object AccountQueries {

    const val FIND_DASHBOARD_BY_USER_ID = """
    SELECT
        -- Account
        a.id                    AS account_id,
        a.bank_id               AS account_bank_id,
        a.name                  AS account_name,
        a.balance               AS account_balance,
        a.currency_id           AS account_currency_id,
        a.created_at            AS account_created_at,
        a.closed_at             AS account_closed_at,
        a.account_status_id     AS account_status_id,
        a.description           AS account_description,
        at.code                 AS account_type,

        -- Bank
        b.id                    AS bank_id,
        b.name                  AS bank_name,
        b.bic                   AS bank_bic,
        b.logo                  AS bank_logo,

        -- Currency
        c.id                    AS currency_id,
        c.code                  AS currency_code,
        c.name                  AS currency_name,
        c.symbol                AS currency_symbol,

        -- AccountStatus
        s.id                    AS status_id,
        s.code                  AS status_code,
        s.name                  AS status_name,

        -- AccountOwner
        ao.id                   AS owner_id,
        ao.account_id           AS owner_account_id,
        ao.user_id              AS owner_user_id,
        ao.name                 AS owner_name,
        ao.ownership_percentage AS owner_ownership_percentage

    FROM accounts a

    JOIN banks b
        ON b.id = a.bank_id

    JOIN currencies c
        ON c.id = a.currency_id

    JOIN account_statuses s
        ON s.id = a.account_status_id

    JOIN account_types at
        ON at.id = a.account_type_id

    JOIN account_owners ao
        ON ao.account_id = a.id

    WHERE EXISTS (
        SELECT 1
        FROM account_owners ao_user
        WHERE ao_user.account_id = a.id
          AND ao_user.user_id = :userId
    )

    ORDER BY a.id
"""

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
               a.description,
               a.account_type_id
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