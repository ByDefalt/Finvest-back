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


class RefreshAccessTokenUseCase(
    private val tokenValidator: TokenValidator,
    private val tokenGenerator: TokenGenerator,
    private val userRepository: UserRepository,
    private val personalInformationRepository: PersonalInformationRepository,
    private val logger: Logger,
) {
    operator fun invoke(refreshToken: String): AccessToken {
        val userId = tokenValidator.validateRefreshToken(refreshToken)

        if (userId == null) {
            logger.warn("invalid refresh token")
            throw IllegalArgumentException("Invalid refresh token")
        }

        val user =
            userRepository.findUserById(UserId(userId))
                ?: throw CustomException(
                    message = "User with id $userId does not exist",
                    code = AuthErrorCode.USER_NOT_FOUND,
                )

        val personalInformation = personalInformationRepository.findById(user.personalInformationId)
            ?: throw IllegalStateException("Personal information not found for user ${user.id}")

        return AccessToken(
            tokenGenerator.generateAccessToken(
                user.id,
                personalInformation.email,
            ),
        )
    }
}
