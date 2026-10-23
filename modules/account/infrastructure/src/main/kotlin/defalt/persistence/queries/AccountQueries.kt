package defalt.persistence.queries

/** SQL queries of the `accounts` table, used by [defalt.persistence.repository.AccountJdbcRepository]. */
object AccountQueries {
    /** Inserts an account; its id is generated. */
    const val INSERT = """
        INSERT INTO accounts (
            bank_bic, name, balance, currency_code, created_at, closed_at,
            account_status_code, description, account_type_code
        ) VALUES (
            :bankBic, :name, :balance, :currencyCode, :createdAt, :closedAt,
            :accountStatusCode, :description, :accountTypeCode
        )
    """

    /** Updates the editable fields of an account (`:accountId`); its type and creation date are kept. */
    const val UPDATE = """
        UPDATE accounts
        SET bank_bic = :bankBic,
            name = :name,
            balance = :balance,
            currency_code = :currencyCode,
            closed_at = :closedAt,
            account_status_code = :accountStatusCode,
            description = :description
        WHERE id = :accountId
    """

    /** Deletes an account (`:accountId`). */
    const val DELETE = "DELETE FROM accounts WHERE id = :accountId"

    /** Accounts of a list of ids (`:accountIds`). */
    const val GET_BY_ACCOUNT_IDS = """
        SELECT id, bank_bic, name, balance, currency_code, created_at, closed_at,
               account_status_code, description, account_type_code
        FROM accounts
        WHERE id IN (:accountIds)
    """

    /** Accounts owned by an internal user (`:userId`), with their bank. */
    const val GET_ACCOUNTS_WITH_BANKS_BY_USER_ID = """
        SELECT a.id, a.bank_bic, b.name as bank_name, b.logo as bank_logo, a.name, a.balance, a.currency_code, a.created_at, a.closed_at,
               a.account_status_code, a.description, a.account_type_code
        FROM accounts a
        JOIN banks b ON b.bic = a.bank_bic
        JOIN account_owners ao ON ao.account_id = a.id
        WHERE ao.user_id = :userId
    """

    /** Accounts owned by an internal user (`:userId`). */
    const val GET_BY_USER_ID = """
        SELECT a.id, a.bank_bic, a.name, a.balance, a.currency_code, a.created_at, a.closed_at,
               a.account_status_code, a.description, a.account_type_code
        FROM accounts a
        JOIN account_owners ao ON ao.account_id = a.id
        WHERE ao.user_id = :userId
    """

    /** One account (`:accountId`). */
    const val GET_BY_ID = """
        SELECT id, bank_bic, name, balance, currency_code, created_at, closed_at,
               account_status_code, description, account_type_code
        FROM accounts
        WHERE id = :accountId
    """
}
