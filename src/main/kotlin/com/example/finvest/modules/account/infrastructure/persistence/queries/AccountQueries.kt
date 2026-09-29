package com.example.finvest.modules.account.infrastructure.persistence.queries

object AccountQueries {
    const val GET_ACCOUNT_BY_USER_ID = """
        SELECT
            a.id AS accountId,
            a.bank_id AS bankId,
            a.name AS name,
            a.balance AS balance,
            a.currency_id AS currencyId,
            a.created_at AS createdAt,
            a.closed_at AS closedAt,
            a.account_status_id AS accountStatusId,
            a.description AS description,
            a.account_type_id AS accountTypeId
        FROM accounts a
        JOIN account_owners ao
            ON ao.account_id = a.id
        WHERE ao.user_id = :userId;
    """

    const val CREATE_ACCOUNT = """
        INSERT INTO accounts (
            bank_id, 
            name, 
            balance,
            currency_id,
            created_at,
            closed_at,
            account_status_id, 
            description, 
            account_type_id
        )
        VALUES (
            :bankId,
            :name,
            :balance,
            :currencyId,
            :createdAt,
            :closedAt,
            :accountStatusId,
            :description, 
            :accountTypeId
        )
    """

    const val UPDATE_ACCOUNT = """
        
        UPDATE accounts
        SET 
            bank_id = :bankId,
            name = :name,
            balance = :balance,
            currency_id = :currencyId,
            created_at = :createdAt,
            closed_at = :closedAt,
            account_status_id = :accountStatusId,
            description = :description,
            account_type_id = :accountTypeId
        WHERE id = :accountId
    """

    const val DELETE_ACCOUNT = """
        DELETE FROM accounts
        WHERE id = :accountId
    """
}
