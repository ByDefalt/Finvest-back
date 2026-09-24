package com.example.finvest.modules.account.infrastructure.persistence.queries

object AssuranceVieQueries {

    const val GET_ASSURANCE_VIE_BY_USER_ID = """
        SELECT
            av.account_id AS accountId,
            av.contract_number AS contractNumber,
            av.opening_date AS openingDate,
            av.management_type_id AS managementTypeId
        FROM assurances_vie av
        JOIN accounts a
            ON a.id = av.account_id
        JOIN account_owners ao
            ON ao.account_id = a.id
        WHERE ao.user_id = :userId;
    """

}