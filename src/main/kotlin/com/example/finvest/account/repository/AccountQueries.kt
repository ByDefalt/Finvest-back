package com.example.finvest.account.repository

object AccountQueries {

    const val FIND_BY_ID = """
        SELECT id, name, balance, type, user_id
        FROM accounts
        WHERE id = :id
    """

    const val FIND_ALL_BY_USER_ID = """
        SELECT id, name, balance, type, user_id
        FROM accounts
        WHERE user_id = :id
    """

    const val DELETE_BY_ID_AND_USER_ID = """
        DELETE FROM accounts
        WHERE id = :id AND user_id = :userId
    """

    const val INSERT_ACCOUNT = """
        INSERT INTO accounts (name, balance, type, user_id)
        VALUES (:name, :balance, :type, :userId)
    """

    const val UPDATE_ACCOUNT = """
        UPDATE accounts
        SET name = :name, balance = :balance, type = :type
        WHERE id = :id AND user_id = :userId
    """


}
