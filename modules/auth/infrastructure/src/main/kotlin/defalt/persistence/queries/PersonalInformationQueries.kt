package defalt.persistence.queries

object PersonalInformationQueries {

    const val CREATE_PERSONAL_INFORMATION = """
        INSERT INTO personal_informations (first_name, last_name, email)
        VALUES (:firstName, :lastName, :email)
    """

    const val GET_PERSONAL_INFO_BY_ID = """
        SELECT id, first_name, last_name, email
        FROM personal_informations
        WHERE id = :id
    """
}