package com.example.finvest.modules.account.infrastructure.persistence.queries

object AccountStatusQueries {
    const val GET_ACCOUNT_STATUSES = """
        SELECT id, code
        FROM account_statuses
    """
}
