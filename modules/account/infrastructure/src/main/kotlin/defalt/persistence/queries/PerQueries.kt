package defalt.persistence.queries

/** SQL queries of the `pers` table (PER details). */
object PerQueries {
    /** Details of a list of accounts (`:accountIds`). */
    const val GET_BY_ACCOUNT_IDS = """
        SELECT account_id, contract_number, opening_date, management_type_code
        FROM pers
        WHERE account_id IN (:accountIds)
    """

    /** Details of the accounts owned by an internal user (`:userId`). */
    const val GET_BY_USER_ID = """
        SELECT p.account_id, p.contract_number, p.opening_date, p.management_type_code
        FROM pers p
        JOIN account_owners ao ON ao.account_id = p.account_id
        WHERE ao.user_id = :userId
    """

    /** Details of one account (`:accountId`). */
    const val GET_BY_ID = """
        SELECT account_id, contract_number, opening_date, management_type_code
        FROM pers
        WHERE account_id = :accountId
    """

    /** Inserts the details of an account. */
    const val INSERT = """
        INSERT INTO pers (account_id, contract_number, opening_date, management_type_code)
        VALUES (:accountId, :contractNumber, :openingDate, :managementTypeCode)
    """

    /** Updates the details of an account (`:accountId`). */
    const val UPDATE = """
        UPDATE pers
        SET contract_number = :contractNumber,
            opening_date = :openingDate,
            management_type_code = :managementTypeCode
        WHERE account_id = :accountId
    """

    /** Deletes the details of an account (`:accountId`). */
    const val DELETE = "DELETE FROM pers WHERE account_id = :accountId"
}
