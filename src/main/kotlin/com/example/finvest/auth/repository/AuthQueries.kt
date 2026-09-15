package com.example.finvest.auth.repository

object AuthQueries {
    const val REGISTER = """
        INSERT INTO users (email, password)
        VALUES (:email, :password);
    """

    const val FIND_BY_EMAIL = """
        SELECT id, email, password
        FROM users
        WHERE email = :email
    """

    const val FIND_BY_ID = """
        SELECT id, email, password
        FROM users
        WHERE id = :userId
    """
}