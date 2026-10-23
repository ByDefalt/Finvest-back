package defalt.persistence.queries

/** SQL queries of the `users` table (internal users). */
object UserQueries {
    /** Internal users of a list of ids (`:userIds`), with their identity. */
    const val FIND_ALL_BY_IDS = """
        SELECT u.id, u.personal_information_id, pi.first_name, pi.last_name, pi.email
        FROM users u
        JOIN personal_informations pi ON pi.id = u.personal_information_id
        WHERE u.id IN (:userIds)
    """

    /** Internal users of a list of emails (`:emails`), with their identity. */
    const val FIND_ALL_BY_EMAILS = """
        SELECT u.id, u.personal_information_id, pi.first_name, pi.last_name, pi.email
        FROM users u
        JOIN personal_informations pi ON pi.id = u.personal_information_id
        WHERE pi.email IN (:emails)
    """

    /** Inserts an internal user with the hash of its password. */
    const val REGISTER = """
        INSERT INTO users (personal_information_id, password)
        VALUES (:personalInformationId, :password);
    """

    /** One internal user (`:userId`). */
    const val FIND_BY_ID = """
        SELECT id, personal_information_id, password
        FROM users
        WHERE id = :userId
    """

    /** Credentials of the internal user with an email (`:email`). */
    const val FIND_CREDENTIALS_BY_EMAIL = """
        SELECT us.id, us.personal_information_id, us.password
        FROM users us
        JOIN personal_informations pi ON us.personal_information_id = pi.id
        WHERE pi.email = :email
    """
}
