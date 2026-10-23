package defalt.service

/** Port of the hashing of the passwords. */
interface PasswordHasher {
    /**
     * Salted hash of [password], to be stored instead of the password.
     *
     * @param password password in clear text.
     * @return the salted hash of the password.
     */
    fun hash(password: String): String

    /**
     * Whether [password] is the password hashed in [hash].
     *
     * @param password password in clear text.
     * @param hash hash stored for the user.
     * @return true if the password matches the hash.
     */
    fun matches(
        password: String,
        hash: String,
    ): Boolean
}
