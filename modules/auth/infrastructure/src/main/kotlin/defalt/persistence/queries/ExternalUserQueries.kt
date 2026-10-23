package defalt.persistence.queries

/** SQL queries of the `users_external` table. */
object ExternalUserQueries {
    /** External users of a list of ids (`:userIds`), with their identity. */
    const val FIND_ALL_BY_IDS = """
        SELECT eu.id, eu.personal_information_id, pi.first_name, pi.last_name, pi.email
        FROM users_external eu
        JOIN personal_informations pi ON pi.id = eu.personal_information_id
        WHERE eu.id IN (:userIds)
    """

    /** External users of a list of emails (`:emails`), with their identity. */
    const val FIND_ALL_BY_EMAILS = """
        SELECT eu.id, eu.personal_information_id, pi.first_name, pi.last_name, pi.email
        FROM users_external eu
        JOIN personal_informations pi ON pi.id = eu.personal_information_id
        WHERE pi.email IN (:emails)
    """

    /** Inserts an external user linked to its identity. */
    const val CREATE_EXTERNAL_USER = """
        INSERT INTO users_external (personal_information_id)
        VALUES (:personalInformationId)
    """
}
