package defalt.persistence.models

data class UserEntity(
    val id: Long,
    val personalInformationId: Long,
    val password: String,
)
