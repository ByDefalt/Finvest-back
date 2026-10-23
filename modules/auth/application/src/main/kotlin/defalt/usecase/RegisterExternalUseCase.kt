package defalt.usecase

import defalt.exception.AuthErrorCode
import defalt.exception.CommonErrorCode
import defalt.exception.CustomException
import defalt.logger.Logger
import defalt.manager.TransactionManager
import defalt.repository.ExternalUserRepository
import defalt.repository.PersonalInformationRepository

/**
 * Registers an external user, who can co-own accounts without logging in.
 *
 * @param externalUserRepository persistence of the external users.
 * @param personalInformationRepository persistence of the identities of the users.
 * @param transactionManager runs the database work in transactions.
 * @param logger logger of the class.
 */
class RegisterExternalUseCase(
    private val externalUserRepository: ExternalUserRepository,
    private val personalInformationRepository: PersonalInformationRepository,
    private val transactionManager: TransactionManager,
    private val logger: Logger,
) {
    /**
     * Creates the identity and the external user in a single transaction.
     *
     * @param firstName first name of the user.
     * @param lastName last name of the user.
     * @param email email of the user, unique.
     * @throws CustomException `EMAIL_ALREADY_USED` if the email is used, `CREATION_FAILED` on a technical failure.
     */
    operator fun invoke(
        firstName: String,
        lastName: String,
        email: String,
    ) {
        logger.info("Registering external user with email: $email")
        transactionManager
            .execute {
                if (personalInformationRepository.existsByEmail(email)) {
                    throw CustomException(AuthErrorCode.EMAIL_ALREADY_USED, "Email $email is already used")
                }
                val personalInformationId = personalInformationRepository.create(firstName, lastName, email)
                externalUserRepository.create(personalInformationId)
            }.onFailure {
                logger.error("Failed to register external user with email: $email", it)
                throw it as? CustomException ?: CustomException(
                    CommonErrorCode.CREATION_FAILED,
                    "Failed to register external user with email: $email",
                    it,
                )
            }
    }
}
