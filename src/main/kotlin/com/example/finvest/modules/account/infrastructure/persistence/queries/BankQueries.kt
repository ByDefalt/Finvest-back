package com.example.finvest.modules.account.infrastructure.persistence.queries

object BankQueries {
    const val GET_BANKS = """
        SELECT id, name, bic, logo
        FROM banks
    """
}
