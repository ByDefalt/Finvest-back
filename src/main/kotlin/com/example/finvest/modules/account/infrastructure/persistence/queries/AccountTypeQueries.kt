package com.example.finvest.modules.account.infrastructure.persistence.queries

object AccountTypeQueries {
    const val GET_ACCOUNT_TYPES = """
        SELECT id, code
        FROM account_types
    """
}
