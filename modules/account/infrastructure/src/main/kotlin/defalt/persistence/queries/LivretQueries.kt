package defalt.persistence.queries

/** SQL queries of the `livrets` table (regulated savings account details). */
object LivretQueries {
    /** Details of a list of accounts (`:accountIds`). */
    const val GET_BY_ACCOUNT_IDS = """
        SELECT account_id, interest_rate, ceiling
        FROM livrets
        WHERE account_id IN (:accountIds)
    """

    /** Details of the accounts owned by an internal user (`:userId`). */
    const val GET_BY_USER_ID = """
        SELECT l.account_id, l.interest_rate, l.ceiling
        FROM livrets l
        JOIN account_owners ao ON ao.account_id = l.account_id
        WHERE ao.user_id = :userId
    """

    /** Details of one account (`:accountId`). */
    const val GET_BY_ID = """
        SELECT account_id, interest_rate, ceiling
        FROM livrets
        WHERE account_id = :accountId
    """

    /** Inserts the details of an account. */
    const val INSERT = """
        INSERT INTO livrets (account_id, interest_rate, ceiling)
        VALUES (:accountId, :interestRate, :ceiling)
    """

    /** Updates the details of an account (`:accountId`). */
    const val UPDATE = """
        UPDATE livrets
        SET interest_rate = :interestRate,
            ceiling = :ceiling
        WHERE account_id = :accountId
    """

    /** Deletes the details of an account (`:accountId`). */
    const val DELETE = "DELETE FROM livrets WHERE account_id = :accountId"
}
