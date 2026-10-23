package defalt.usecase

import defalt.exception.AuthErrorCode
import defalt.exception.CommonErrorCode
import defalt.exception.CustomException
import defalt.logger.Logger
import defalt.manager.TransactionManager
import defalt.repository.PersonalInformationRepository
import defalt.repository.UserRepository
import defalt.service.PasswordHasher

/**
 * Registers an internal user, who can log in.
 *
 * @param userRepository persistence of the internal users.
 * @param personalInformationRepository persistence of the identities of the users.
 * @param transactionManager runs the database work in transactions.
 * @param passwordHasher hashes and checks the passwords.
 * @param logger logger of the class.
 */
class RegisterUseCase(
    private val userRepository: UserRepository,
    private val personalInformationRepository: PersonalInformationRepository,
    private val transactionManager: TransactionManager,
    private val passwordHasher: PasswordHasher,
    private val logger: Logger,
) {
    /**
     * Hashes the password, then creates the identity and the user in a single transaction.
     *
     * @param email email of the user, unique.
     * @param firstName first name of the user.
     * @param lastName last name of the user.
     * @param password password in clear text, stored hashed.
     * @throws CustomException `EMAIL_ALREADY_USED` if the email is used, `CREATION_FAILED` on a technical failure.
     */
    operator fun invoke(
        email: String,
        firstName: String,
        lastName: String,
        password: String,
    ) {
        val hashedPassword = passwordHasher.hash(password)
        transactionManager
            .execute {
                if (personalInformationRepository.existsByEmail(email)) {
                    throw CustomException(AuthErrorCode.EMAIL_ALREADY_USED, "Email $email is already used")
                }
                val personalInformationId = personalInformationRepository.create(firstName, lastName, email)
                userRepository.register(personalInformationId, hashedPassword)
            }.onFailure {
                logger.error("Failed to register user: email=$email, error=${it.message}")
                throw it as? CustomException ?: CustomException(CommonErrorCode.CREATION_FAILED, "Failed to register user $email", it)
            }
    }
}
