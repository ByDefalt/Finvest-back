package com.example.finvest.modules.account.infrastructure.persistence.queries

object AccountQueries {
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
