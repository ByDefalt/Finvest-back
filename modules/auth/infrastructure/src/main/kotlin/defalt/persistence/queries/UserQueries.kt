package defalt.persistence.queries

object UserQueries {
    const val REGISTER = """
        INSERT INTO users (personal_information_id, password)
        VALUES (:personalInformationId, :password);
    """

    const val FIND_BY_EMAIL = """
        SELECT us.id, us.personal_information_id
        FROM users us
        JOIN personal_informations pi ON us.personal_information_id = pi.id
        WHERE pi.email = :email
    """

    const val FIND_BY_ID = """
        SELECT id, personal_information_id, password
        FROM users
        WHERE id = :userId
    """

    const val FIND_CREDENTIALS_BY_EMAIL = """
        SELECT us.id, us.personal_information_id, us.password
        FROM users us
        JOIN personal_informations pi ON us.personal_information_id = pi.id
        WHERE pi.email = :email
    """
}
