package defalt.persistence.queries

/** SQL queries of the `account_owners` table, used by [defalt.persistence.repository.AccountOwnerJdbcRepository]. */
object AccountOwnerQueries {
    /** Inserts an owner: either `:userId` (internal user) or `:userExternalId` (external user) is set. */
    const val INSERT = """
        INSERT INTO account_owners (
            account_id,
            user_id,
            user_external_id,
            ownership_percentage
        )
        VALUES (
            :accountId,
            :userId,
            :userExternalId,
            :ownershipPercentage
        )
    """

    /** Owners of a list of accounts (`:accountIds`). */
    const val GET_BY_ACCOUNT_IDS = """
        SELECT id, account_id, user_id, user_external_id, ownership_percentage
        FROM account_owners
        WHERE account_id IN (:accountIds)
    """

    /** Deletes every owner of an account (`:accountId`). */
    const val DELETE_BY_ACCOUNT_ID = "DELETE FROM account_owners WHERE account_id = :accountId"
}
