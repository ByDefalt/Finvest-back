package defalt.persistence.queries

/** SQL queries of the `peas` table (PEA details). */
object PeaQueries {
    /** Details of a list of accounts (`:accountIds`). */
    const val GET_BY_ACCOUNT_IDS = """
        SELECT account_id, opening_date, deposit_limit
        FROM peas
        WHERE account_id IN (:accountIds)
    """

    /** Details of the accounts owned by an internal user (`:userId`). */
    const val GET_BY_USER_ID = """
        SELECT p.account_id, p.opening_date, p.deposit_limit
        FROM peas p
        JOIN account_owners ao ON ao.account_id = p.account_id
        WHERE ao.user_id = :userId
    """

    /** Details of one account (`:accountId`). */
    const val GET_BY_ID = """
        SELECT account_id, opening_date, deposit_limit
        FROM peas
        WHERE account_id = :accountId
    """

    /** Inserts the details of an account. */
    const val INSERT = """
        INSERT INTO peas (account_id, opening_date, deposit_limit)
        VALUES (:accountId, :openingDate, :depositLimit)
    """

    /** Updates the details of an account (`:accountId`). */
    const val UPDATE = """
        UPDATE peas
        SET opening_date = :openingDate,
            deposit_limit = :depositLimit
        WHERE account_id = :accountId
    """

    /** Deletes the details of an account (`:accountId`). */
    const val DELETE = "DELETE FROM peas WHERE account_id = :accountId"
}
