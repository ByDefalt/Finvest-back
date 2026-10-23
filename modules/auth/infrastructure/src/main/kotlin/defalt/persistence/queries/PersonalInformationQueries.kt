package defalt.persistence.queries

/** SQL queries of the `personal_informations` table. */
object PersonalInformationQueries {
    /** Inserts the identity of a user. */
    const val CREATE_PERSONAL_INFORMATION = """
        INSERT INTO personal_informations (first_name, last_name, email)
        VALUES (:firstName, :lastName, :email)
    """

    /** One identity (`:id`). */
    const val GET_PERSONAL_INFO_BY_ID = """
        SELECT id, first_name, last_name, email
        FROM personal_informations
        WHERE id = :id
    """

    /** Number of identities using an email (`:email`): 0 or 1. */
    const val EXISTS_BY_EMAIL = "SELECT COUNT(*) FROM personal_informations WHERE email = :email"
}
