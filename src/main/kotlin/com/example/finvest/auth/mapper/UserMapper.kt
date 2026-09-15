package com.example.finvest.auth.mapper

import com.example.finvest.auth.domain.User
import com.example.finvest.auth.domain.UserCredentials
import com.example.finvest.auth.dto.UserResponse
import com.example.finvest.auth.entity.UserEntity

fun UserEntity.toUserCredentialsDomain(): UserCredentials {
    return UserCredentials(
        id = requireNotNull(this.id),
        email = this.email,
        password = this.password
    )
}

fun UserEntity.toUserDomain(): User {
    return User(
        id = requireNotNull(this.id),
        email = this.email
    )
}

fun User.toDto(): UserResponse {
    return UserResponse(
        email = this.email
    )
}