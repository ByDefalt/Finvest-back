package com.example.finvest.modules.auth.infrastructure.persistence.mapper

import com.example.finvest.modules.auth.domain.models.User
import com.example.finvest.modules.auth.domain.models.UserCredentials
import com.example.finvest.modules.auth.infrastructure.persistence.models.UserEntity

fun UserEntity.toDomain(): User =
    User(
        id = this.id,
        email = this.email,
    )

fun UserEntity.toDomainCredentials(): UserCredentials =
    UserCredentials(
        id = this.id,
        email = this.email,
        password = this.password,
    )
