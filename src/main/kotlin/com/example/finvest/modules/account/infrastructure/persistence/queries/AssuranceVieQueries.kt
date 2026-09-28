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
        WHERE ao.user_id = :userId
    """

    const val CREATE_ASSURANCE_VIE = """
        INSERT INTO assurances_vie (account_id, contract_number, opening_date, management_type_id)
        VALUES (:accountId, :contractNumber, :openingDate, :managementTypeId)
    """

    const val UPDATE_ASSURANCE_VIE_BY_ACCOUNT_ID = """
        UPDATE assurances_vie
        SET contract_number = :contractNumber,
            opening_date = :openingDate,
            management_type_id = :managementTypeId
        WHERE account_id = :accountId
    """

    const val DELETE_ASSURANCE_VIE_BY_ACCOUNT_ID = """
        DELETE FROM assurances_vie
        WHERE account_id = :accountId
    """
}
