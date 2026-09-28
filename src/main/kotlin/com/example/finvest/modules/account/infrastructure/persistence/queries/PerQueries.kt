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

    const val CREATE_PER = """
        INSERT INTO pers (account_id, contract_number, opening_date, management_type_id)
        VALUES (:accountId, :contractNumber, :openingDate, :managementTypeId);
    """

    const val UPDATE_PER_BY_ACCOUNT_ID = """
        UPDATE pers
        SET contract_number = :contractNumber,
            opening_date = :openingDate,
            management_type_id = :managementTypeId
        WHERE account_id = :accountId;
    """

    const val DELETE_PER_BY_ACCOUNT_ID = """
        DELETE FROM pers
        WHERE account_id = :accountId;
    """
}
