package defalt.usecase

import defalt.exception.AuthErrorCode
import defalt.exception.CustomException
import defalt.logger.Logger
import defalt.repository.PersonalInformationRepository
import defalt.repository.UserRepository
import defalt.service.TokenGenerator
import defalt.service.TokenValidator
import defalt.valueobject.AccessToken
import defalt.valueobject.UserId

/**
 * Exchanges a refresh token for a new access token.
 *
 * @param tokenValidator validates the authentication tokens.
 * @param tokenGenerator generates the authentication tokens.
 * @param userRepository persistence of the internal users.
 * @param personalInformationRepository persistence of the identities of the users.
 * @param logger logger of the class.
 */
class RefreshAccessTokenUseCase(
    private val tokenValidator: TokenValidator,
    private val tokenGenerator: TokenGenerator,
    private val userRepository: UserRepository,
    private val personalInformationRepository: PersonalInformationRepository,
    private val logger: Logger,
) {
    /**
     * Access token of the user of [refreshToken].
     *
     * @param refreshToken refresh token of the cookie.
     * @return a new access token.
     * @throws CustomException `INVALID_REFRESH_TOKEN` if the refresh token is invalid or expired,
     * `USER_NOT_FOUND` if its user no longer exists, `PERSONAL_INFORMATION_NOT_FOUND` if the data are inconsistent.
     */
    operator fun invoke(refreshToken: String): AccessToken {
        val userId = tokenValidator.validateRefreshToken(refreshToken)

        if (userId == null) {
            logger.warn("invalid refresh token")
            throw CustomException(AuthErrorCode.INVALID_REFRESH_TOKEN, "Invalid or expired refresh token")
        }

        val user =
            userRepository.findUserById(UserId(userId))
                ?: throw CustomException(
                    message = "User with id $userId does not exist",
                    code = AuthErrorCode.USER_NOT_FOUND,
                )

        val personalInformation =
            personalInformationRepository.findById(user.personalInformationId)
                ?: throw CustomException(
                    AuthErrorCode.PERSONAL_INFORMATION_NOT_FOUND,
                    "Personal information not found for user ${user.id.value}",
                )

        return AccessToken(
            tokenGenerator.generateAccessToken(
                user.id,
                personalInformation.email,
            ),
        )
    }
}
