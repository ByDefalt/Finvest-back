package defalt.usecase

import defalt.exception.AuthErrorCode
import defalt.exception.CustomException
import defalt.logger.Logger
import defalt.repository.UserRepository
import defalt.service.PasswordHasher
import defalt.service.TokenGenerator
import defalt.valueobject.Email
import defalt.valueobject.RefreshToken

/**
 * Logs an internal user in.
 *
 * @param userRepository persistence of the internal users.
 * @param passwordHasher hashes and checks the passwords.
 * @param tokenGenerator generates the authentication tokens.
 * @param logger logger of the class.
 */
class LoginUseCase(
    private val userRepository: UserRepository,
    private val passwordHasher: PasswordHasher,
    private val tokenGenerator: TokenGenerator,
    private val logger: Logger,
) {
    /**
     * Checks the credentials and returns a refresh token, to be exchanged for access tokens.
     *
     * @param email email of the internal user.
     * @param password password in clear text.
     * @return the refresh token of the user.
     * @throws CustomException `USER_NOT_FOUND` if no internal user has [email], `INVALID_PASSWORD` if the password is wrong.
     */
    operator fun invoke(
        email: String,
        password: String,
    ): RefreshToken {
        val existingUser = userRepository.findCredentialsByEmail(email)

        if (existingUser == null) {
            logger.warn("user not found email=$email")
            throw CustomException(
                message = "User with email $email does not exist",
                code = AuthErrorCode.USER_NOT_FOUND,
            )
        }

        if (!passwordHasher.matches(
                password,
                existingUser.password,
            )
        ) {
            logger.warn("invalid password for email=$email")
            throw CustomException(
                message = "Invalid password for user with email $email",
                code = AuthErrorCode.INVALID_PASSWORD,
            )
        }

        val refreshToken = tokenGenerator.generateRefreshToken(existingUser.id, Email(email))

        logger.info(
            "AuthService.login - success id=${existingUser.id} email=$email",
        )

        return RefreshToken(refreshToken)
    }
}
