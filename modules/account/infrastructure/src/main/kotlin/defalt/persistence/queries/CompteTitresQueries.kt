package defalt.persistence.queries

/** SQL queries of the `comptes_titres` table (securities account details). */
object CompteTitresQueries {
    /** Details of a list of accounts (`:accountIds`). */
    const val GET_BY_ACCOUNT_IDS = """
        SELECT account_id, account_number
        FROM comptes_titres
        WHERE account_id IN (:accountIds)
    """

    /** Details of the accounts owned by an internal user (`:userId`). */
    const val GET_BY_USER_ID = """
        SELECT ct.account_id, ct.account_number
        FROM comptes_titres ct
        JOIN account_owners ao ON ao.account_id = ct.account_id
        WHERE ao.user_id = :userId
    """

    /** Details of one account (`:accountId`). */
    const val GET_BY_ID = """
        SELECT account_id, account_number
        FROM comptes_titres
        WHERE account_id = :accountId
    """

    /** Inserts the details of an account. */
    const val INSERT = """
        INSERT INTO comptes_titres (account_id, account_number)
        VALUES (:accountId, :accountNumber)
    """

    /** Updates the details of an account (`:accountId`). */
    const val UPDATE = """
        UPDATE comptes_titres
        SET account_number = :accountNumber
        WHERE account_id = :accountId
    """

    /** Deletes the details of an account (`:accountId`). */
    const val DELETE = "DELETE FROM comptes_titres WHERE account_id = :accountId"
}
