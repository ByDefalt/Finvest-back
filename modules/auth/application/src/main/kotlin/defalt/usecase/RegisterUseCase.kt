package defalt.usecase

import defalt.logger.Logger
import defalt.manager.TransactionManager
import defalt.models.PersonalInformation
import defalt.models.User
import defalt.models.UserWithPersonalInformation
import defalt.repository.PersonalInformationRepository
import defalt.repository.UserRepository
import defalt.service.PasswordHasher


class RegisterUseCase(
    private val userRepository: UserRepository,
    private val personalInformationRepository: PersonalInformationRepository,
    private val transactionManager: TransactionManager,
    private val passwordHasher: PasswordHasher,
    private val logger: Logger,
) {
    operator fun invoke(
        email: String,
        firstName: String,
        lastName: String,
        password: String,
    ) {
        val hashedPassword = passwordHasher.hash(password)
        transactionManager.execute {
            val personalInformationId = personalInformationRepository.create(firstName, lastName, email)
            userRepository.register(personalInformationId, hashedPassword)
        }.onFailure {
            logger.error("Failed to register user: email=$email, error=${it.message}")
            throw it
        }
    }
}