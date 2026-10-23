package defalt.persistence.queries

/** SQL queries of the `pees` table (PEE details). */
object PeeQueries {
    /** Details of a list of accounts (`:accountIds`). */
    const val GET_BY_ACCOUNT_IDS = """
        SELECT account_id, opening_date, employer
        FROM pees
        WHERE account_id IN (:accountIds)
    """

    /** Details of the accounts owned by an internal user (`:userId`). */
    const val GET_BY_USER_ID = """
        SELECT pee.account_id, pee.opening_date, pee.employer
        FROM pees pee
        JOIN account_owners ao ON ao.account_id = pee.account_id
        WHERE ao.user_id = :userId
    """

    /** Details of one account (`:accountId`). */
    const val GET_BY_ID = """
        SELECT account_id, opening_date, employer
        FROM pees
        WHERE account_id = :accountId
    """

    /** Inserts the details of an account. */
    const val INSERT = """
        INSERT INTO pees (account_id, opening_date, employer)
        VALUES (:accountId, :openingDate, :employer)
    """

    /** Updates the details of an account (`:accountId`). */
    const val UPDATE = """
        UPDATE pees
        SET opening_date = :openingDate,
            employer = :employer
        WHERE account_id = :accountId
    """

    /** Deletes the details of an account (`:accountId`). */
    const val DELETE = "DELETE FROM pees WHERE account_id = :accountId"
}
