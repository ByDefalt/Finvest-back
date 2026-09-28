package com.example.finvest.modules.account.infrastructure.persistence.queries

object CurrencyQueries {
    const val GET_CURRENCIES = """
        SELECT id, code
        FROM currencies
    """
}
