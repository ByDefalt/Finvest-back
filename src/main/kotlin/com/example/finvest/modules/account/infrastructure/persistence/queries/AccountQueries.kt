package com.example.finvest.modules.account.infrastructure.persistence.queries

object AccountQueries {

    const val FIND_DASHBOARD_BY_USER_ID = """
    SELECT
        -- Account
        a.id                    AS accountId,
        a.bank_id               AS accountBankId,
        a.name                  AS accountName,
        a.balance               AS accountBalance,
        a.currency_id           AS accountCurrencyId,
        a.created_at            AS accountCreatedAt,
        a.closed_at             AS accountClosedAt,
        a.account_status_id     AS accountStatusId,
        a.description           AS accountDescription,
        at.code                 AS accountType,

        -- Bank
        b.id                    AS bankId,
        b.name                  AS bankName,
        b.bic                   AS bankBic,
        b.logo                  AS bankLogo,

        -- Currency
        c.id                    AS currencyId,
        c.code                  AS currencyCode,

        -- AccountStatus
        s.id                    AS statusId,
        s.code                  AS statusCode,

        -- AccountOwner
        ao.id                   AS ownerId,
        ao.account_id           AS ownerAccountId,
        ao.user_id              AS ownerUserId,
        ao.name                 AS ownerName,
        ao.ownership_percentage AS ownerOwnershipPercentage
        

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