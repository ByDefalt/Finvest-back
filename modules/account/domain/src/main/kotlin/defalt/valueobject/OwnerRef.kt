package defalt.valueobject

/** User owning an account: an internal user, an external user, or a user still known only by its email. */
sealed interface OwnerRef {
    /**
     * User with credentials, who can log in.
     *
     * @property userId id of the internal user.
     */
    data class Internal(
        val userId: UserId,
    ) : OwnerRef

    /**
     * User without credentials (relative, partner...), co-owner of accounts.
     *
     * @property externalId id of the external user.
     */
    data class External(
        val externalId: ExternalUserId,
    ) : OwnerRef

    /**
     * Owner not resolved yet: only its email is known. Never persisted.
     *
     * @property email email of the owner, to resolve to a user.
     */
    data class Unknown(
        val email: Email,
    ) : OwnerRef
}
