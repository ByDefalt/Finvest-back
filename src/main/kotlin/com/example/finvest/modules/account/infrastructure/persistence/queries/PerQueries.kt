package com.example.finvest.modules.account.infrastructure.persistence.queries

object PerQueries {

    const val GET_PER_BY_USER_ID = """
        SELECT
            p.account_id AS accountId,
            p.contract_number AS contractNumber,
            p.opening_date AS openingDate,
            p.management_type_id AS managementTypeId
        FROM pers p
        JOIN accounts a
            ON a.id = p.account_id
        JOIN account_owners ao
            ON ao.account_id = a.id
        WHERE ao.user_id = :userId;
    """
}
