package defalt.persistence.queries

/** SQL queries of the `compte_courants` table (current account details). */
object CompteCourantQueries {
    /** Details of a list of accounts (`:accountIds`). */
    const val GET_BY_ACCOUNT_IDS = """
        SELECT account_id, iban, bic, account_number, overdraft_limit, holder_name
        FROM compte_courants
        WHERE account_id IN (:accountIds)
    """

    /** Details of the accounts owned by an internal user (`:userId`). */
    const val GET_BY_USER_ID = """
        SELECT cc.account_id, cc.iban, cc.bic, cc.account_number, cc.overdraft_limit, cc.holder_name
        FROM compte_courants cc
        JOIN account_owners ao ON ao.account_id = cc.account_id
        WHERE ao.user_id = :userId
    """

    /** Details of one account (`:accountId`). */
    const val GET_BY_ID = """
        SELECT account_id, iban, bic, account_number, overdraft_limit, holder_name
        FROM compte_courants
        WHERE account_id = :accountId
    """

    /** Inserts the details of an account. */
    const val INSERT = """
        INSERT INTO compte_courants
            (account_id, iban, bic, account_number, overdraft_limit, holder_name)
        VALUES (:accountId, :iban, :bic, :accountNumber, :overdraftLimit, :holderName)
    """

    /** Updates the details of an account (`:accountId`). */
    const val UPDATE = """
        UPDATE compte_courants
        SET iban = :iban,
            bic = :bic,
            account_number = :accountNumber,
            overdraft_limit = :overdraftLimit,
            holder_name = :holderName
        WHERE account_id = :accountId
    """

    /** Deletes the details of an account (`:accountId`). */
    const val DELETE = "DELETE FROM compte_courants WHERE account_id = :accountId"
}
